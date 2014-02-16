(function() {
  'use strict';

  var boshService = null;
  var userJid = null;
  var password = null;
  var chatBotJid = null;
  var chatBotNick = null;
  var userRole = null;
  var mucRoom = null;
  var userNickname = null;
  var stropheConnection = null;
  
  function getAvatarUrl(userJid, size) {
	  var groupJid = $('#xmpp-room').val();
    var groupUrlName = Strophe.getNodeFromJid(groupJid);
    return CONTEXTPATH + '/illusion/groupAvatar/' + groupUrlName + '/' + userJid + '?size=' + size;
  };
  
  function addParticipant(userJid) {
    dust.render("illusion-group-participant", {
      jid: userJid
    }, function(err, html) {
      $(html).appendTo('.illusion-group-participants');
    });
  };
  
  function joinRoom(roomName, nickname) {
    stropheConnection.muc.join(roomName, Strophe.escapeNode(nickname), function msg_handler_cb(messageXml, room) {
      var from = messageXml.getAttribute('from');
      var to = messageXml.getAttribute('to');
      var bodyElement = messageXml.getElementsByTagName('body')[0];
      var delayElement = messageXml.getElementsByTagName('delay')[0];
      var xElement = messageXml.getElementsByTagName('x')[0];
      var fromJid = xElement ? xElement.getAttribute('from') : null;
      var nick = Strophe.getResourceFromJid(from);
      
      if (!fromJid) {
        var rosterEntry = room.roster[nick];
        if (rosterEntry) {
          fromJid = rosterEntry.jid;
        }
      }
      
      if (fromJid) {
        fromJid = Strophe.getBareJidFromJid(fromJid);
      }
        
      $(document).trigger('strophe.muc.message', {
        message: messageXml,
        room: room,
        from: from,
        to: to,
        nick: nick,
        body: bodyElement ? Strophe.getText(bodyElement) : null,
        delay: delayElement ? delayElement.getAttribute('stamp') : null,
        fromJid: fromJid
      });
      
      return true;
    }, function pres_handler_cb(presenceXml, room) {
      var xElement = presenceXml.getElementsByTagName('x')[0];
      if (xElement) {
        var statusElement = xElement.getElementsByTagName('status')[0];
        if (statusElement && ("110" == statusElement.getAttribute("code"))) {
          $(document).trigger('strophe.muc.join', {
            room: room
          });
          
          return true;
        }
        
        var item = xElement.getElementsByTagName('item')[0];
        if (item) {
          $(document).trigger('strophe.muc.presense', {
            room: room,
            affiliation: item.getAttribute('affiliation'),
            jid: item.getAttribute('jid'),
            nick: item.getAttribute('nick'),
            role: item.getAttribute('role')
          });
          
          return true;
        }
      }

      return true;
    }, function roster_cb(rosterXml, room) {
      return true;
    });
  }
  
  $(document).ready(function() {
    boshService = $('#xmpp-bosh-service').val();
    userJid = $('#xmpp-user-jid').val();
    password = $('#xmpp-password').val();
    chatBotJid = $('#chat-bot-jid').val();
    userRole = $('#user-role').val();
    userNickname = $('#user-nickname').val();
    
    $('.illusion-group-chat-input').attr('disabled', 'disabled');
    $('.illusion-group-user-menu')
      .hide()
      .menu({
        select: function( event, ui ) {
          event.preventDefault();
          $(this).hide();
          var action = ui.item.find('a').data('action');
          $(document).trigger('illusion.user.' + action, { });
        }
      });
    
    $('.illusion-group-admin-menu')
    .hide()
    .menu({
      select: function( event, ui ) {
        event.preventDefault();
        $(this).hide();
        var action = ui.item.find('a').data('action');
        $(document).trigger('illusion.admin.' + action, { });
      }
    });
    
    stropheConnection = new Strophe.Connection(boshService);
    stropheConnection.connect(userJid, password, function (status) {
      if (status == Strophe.Status.CONNFAIL) {
        // TODO: Proper error handling...
        alert('Xmpp Connection Failed');
      } else if (status == Strophe.Status.CONNECTED) {
        $(document).trigger('strophe.connect', {
        });
      };
    }); 
  });
  
  $(window).click(function (event) {
	  if ($(event.target).closest('.illusion-group-participant').length == 0) {
      $('.illusion-group-participant-menu').hide();
    }

    if ($(event.target).closest('.illusion-group-user').length == 0) {
      $('.illusion-group-user-menu').hide();
    }

    if ($(event.target).closest('.illusion-group-admin').length == 0) {
      $('.illusion-group-admin-menu').hide();
    }
  });
  
  $(document).on('strophe.connect', function (event, data) {
    var roomName = $('#xmpp-room').val();
    joinRoom(roomName, userNickname);
  });
  
  $(document).on('strophe.muc.join', function (event, data) {
    var room = mucRoom = data.room;
    
    $('.illusion-group-chat-input').removeAttr('disabled');
    $('.illusion-group-chat-input').keydown(function (event){
      if (event.keyCode == 13) {
        event.preventDefault();
        
        var message = $(this).val();
        if (message) {
          if (message.indexOf('/') == 0) {
            var commandText = message.substring(1);
            var argIndex = commandText.indexOf(' ');
            var command = null;
            var commandArgs = null;

            if (argIndex > -1) {
              command = commandText.substring(0, argIndex);
              commandArgs = commandText.substring(argIndex + 1);
              
              $(document).trigger('illusion.chat.command.' + command, {
                args: commandArgs,
                room: room
              });
            } else {
              command = commandText;
            }
          } else {
            room.groupchat($(this).val());
          }
        }

        $(this).val(null);
      }
    });
  });
  
  $(document).on('strophe.muc.presense', function (event, data) {
    if (Strophe.getBareJidFromJid(data.jid) == chatBotJid) {
	    chatBotNick = data.nick;
	  } else {
      addParticipant(Strophe.getBareJidFromJid(data.jid));
	  }
  });
  
  $(document).on('strophe.muc.message', function (event, data) {
    var time = new Date();

    if (data.delay) {
      time.setTime(Date.parse(data.delay));
    }
    
    var message = $('<div>');
    
    if (data.fromJid) {
      message.append($('<img>')
        .addClass('illusion-group-chat-message-avatar')
        .attr("src", getAvatarUrl(data.fromJid, 32))
      );
    };
    
    message
      .addClass('illusion-group-chat-message')
      .append($('<div>')
        .addClass('illusion-group-chat-message-sent')
        .text(time.toUTCString())
      )
      .append($('<div>')
        .addClass('illusion-group-chat-message-from')
        .text(Strophe.unescapeNode(data.nick))
      )
      .append($('<div>')
        .addClass('illusion-group-chat-message-body')
        .html((data.body||'')
        		.replace(/\n/g, '<br/>')
        		.replace(/\[\+\]/g, '<span class="illusion-group-chat-fudge-plus"/>')
        		.replace(/\[\ \]/g, '<span class="illusion-group-chat-fudge-empty"/>')
        		.replace(/\[\-\]/g, '<span class="illusion-group-chat-fudge-minus"/>'))
      );
    
    var messages = $('.illusion-group-chat-messages');
    
    messages
      .append(message)
      .scrollTo(message);
  });

  $(document).on("click", '.illusion-group-user .illusion-group-user-image', function (event) {
    $(this).closest('.illusion-group-user').find('.illusion-group-user-menu').show();
  });
  
  $(document).on("click", '.illusion-group-participant .illusion-group-participant-image', function (event) {
	  $(this).closest('.illusion-group-participant').find('.illusion-group-participant-menu').show();
  });

  $(document).on("click", '.illusion-group-admin-image', function (event) {
    $(this).closest('.illusion-group-admin').find('.illusion-group-admin-menu').show();
  });
  
  $(document).on('illusion.participant.showCharacterSheet', function (event, data) {
	  alert('TODO: Show Character Sheet!');
  });
  
  /* Chat commands */
  
  $(document).on("illusion.chat.command.roll", function (event, data) {
	  var room = data.room;
	  room.groupchat('/roll ' + data.args, null);
  });
  
  $(document).on("illusion.chat.command.invite", function (event, data) {
	  if (data.args) {
	    var room = data.room;
	    // TODO: Only game master should be able to do this...
	    room.invite(data.args);
	  }
  });
  
  $(document).on("illusion.chat.command.changeNick", function (event, data) {
    if (data.args) {
      $('.illusion-group-chat-input').attr('disabled', 'disabled');
      $('input[name="command-form:userNickname"]').val(data.args);
      $('input[name="command-form:updateUserNickname"]').click();
    }
  });
  
  $(document).on("illusion.chat.command.changeBotNick", function (event, data) {
    if (data.args) {
      var room = data.room;
      // TODO: Only game master should be able to do this...
      room.message(chatBotNick, '/roomSetting nick ' + data.args, null, 'chat');
    }
  });
  
  /* User commands */
  
  $(document).on("illusion.user.changeNickname", function (event, data) {
    dust.render("illusion-group-change-nick", {
      name: userNickname
    }, function(err, html) {
      if (!err) {
        var dialog = $(html);
        dialog.dialog({
          modal: true,
          width: 400,
          buttons: [{
            'text': dialog.data('rename-button'),
            'click': function(event) { 
              var name = $(this).find('input[name="name"]').val();
              if (name) {
                $(document).trigger("illusion.chat.command.changeNick", {
                  args: name,
                  room: mucRoom
                });
              }
              
              $(this).dialog("close");
            }
          }, {
            'text': dialog.data('cancel-button'),
            'click': function(event) { 
              $(this).dialog("close");
            }
          }]
        });
      } else {
        // TODO: Proper error handling...
        alert(err);
      }
    }); 
  });
  
  $(document).on("illusion.user.changeAvatar", function (event, data) {
    dust.render("illusion-group-change-avatar", {
      name: userNickname
    }, function(err, html) {
      if (!err) {
        var dialog = $(html);
        
        dialog.imageDialog({
          okButtonText: dialog.data('change-button'),
          cancelButtonText: dialog.data('cancel-button'),
          uploadHintText: dialog.data('upload-hint')
        });
        
        dialog.on('imageDialog.okClick', function (event, data) {
          var groupJid = $('#xmpp-room').val();
          var groupUrlName = Strophe.getNodeFromJid(groupJid);
          
          $.ajax(CONTEXTPATH + '/illusion/groupAvatar/' + groupUrlName + '/' + userJid, {
            type: 'POST',
            data: {
              'data': data.imageData
            },
            success: function (event) {
              window.location.reload(true);
            }
          });
        });
        
      } else {
        // TODO: Proper error handling...
        alert(err);
      }
    }); 
  });
  
  /* Admin commands */
  
  $(document).on("illusion.admin.changeBotNickname", function (event, data) {
    dust.render("illusion-group-rename-chatbot", {
      name: chatBotNick
    }, function(err, html) {
      if (!err) {
        var dialog = $(html);
        dialog.dialog({
          modal: true,
          width: 400,
          buttons: [{
            'text': dialog.data('rename-button'),
            'click': function(event) { 
              var nickname = $(this).find('input[name="name"]').val();
              if (nickname) {
                $(document).trigger("illusion.chat.command.changeBotNick", {
                  args: nickname,
                  room: mucRoom
                });
              }
              
              $(this).dialog("close");
            }
          }, {
            'text': dialog.data('cancel-button'),
            'click': function(event) { 
              $(this).dialog("close");
            }
          }]
        });
      } else {
        // TODO: Proper error handling...
        alert(err);
      }
    }); 
  });
  
}).call(this);