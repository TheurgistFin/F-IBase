(function() {
  'use strict';
  
  function getAvatarUrl(userJid, size) {
	var groupJid = $('#xmpp-room').val();
    var groupUrlName = Strophe.getNodeFromJid(groupJid);
    return CONTEXTPATH + '/illusion/groupAvatar/' + groupUrlName + '/' + userJid + '?size=' + size;
  };
  
  function addParticipant(userJid) {
	  
	$('<div>')
	  .addClass('illusion-group-participant')
	  .append($('<div>')
	    .addClass("illusion-group-participant-image-container")
	    .append($('<img>')
	      .addClass('illusion-group-participant-image')
	      .attr('src', getAvatarUrl(userJid, 90))
	    )
	    .append($('<div>').addClass('illusion-group-participant-image-cover'))
	    .append($('<ul>')
	      .addClass('illusion-group-participant-menu')
	      .append($('<li>')
	        .append(
	    	  $('<a>')
	    	    .attr('href', '#')
                .data('action', 'showCharacterSheet')
                .text('Näytä hahmolomake')
            )
          )
          .append($('<li>')
            .append($('<a>')
  	    	  .attr('href', '#')
  	    	  .text('Hallinnoi')
	    	)
    	    .append($('<ul>')
    	      .append($('<li>')
    	        .append($('<a>')
  	    	      .attr('href', '#')
  	    	      .data('action', 'makeGameMaster')
                  .text('Nosta pelinjohtajaksi') 
                )
    	      )
    	      .append($('<li>')
    	        .append($('<a>')
    	  	      .attr('href', '#')
    	  	      .data('action', 'kick')
    	          .text('Heitä Ulos') 
    	        )
    	      )
  	    	)
          )
          .hide()
          .menu({
            select: function( event, ui ) {
              event.preventDefault();
        	  $(this).hide();
        	  var action = ui.item.find('a').data('action');
        	  $(document).trigger('illusion.menu.' + action, { });
        	}
          })
	    )
	  ).appendTo('.illusion-group-participants');
  };
  
  function showSlash(message) {
    $('.illusion-group-chat-splash-container').append(
      $('<div>')
        .hide()
        .addClass('illusion-group-chat-splash-text')
        .html(message)
        .show("fade")
        .delay(10000)
        .hide("puff")
    );  
  };
  
  var boshService = null;
  var userJid = null;
  var password = null;
  var chatBotJid = null;
  var chatBotNick = null;
  
  $(document).ready(function() {
    boshService = $('#xmpp-bosh-service').val();
    userJid = $('#xmpp-user-jid').val();
    password = $('#xmpp-password').val();
    chatBotJid = $('#chat-bot-jid').val();
    
    $('.illusion-group-chat-input').attr('disabled', 'disabled');
    
    var stropheConnection = new Strophe.Connection(boshService);
    stropheConnection.connect(userJid, password, function (status) {
      if (status == Strophe.Status.CONNFAIL) {
        // TODO: Proper error handling...
        alert('Xmpp Connection Failed');
      } else if (status == Strophe.Status.CONNECTED) {
        $(document).trigger('strophe.connect', {
          stropheConnection: stropheConnection
        });
      };
    }); 
  });
  
  $(window).click(function (event) {
	if ($(event.target).closest('.illusion-group-participant').length == 0) {
      $('.illusion-group-participant-menu').hide();
    }
  });
  
  $(document).on('strophe.connect', function (event, data) {
    var stropheConnection = data.stropheConnection;
    var roomName = $('#xmpp-room').val();
    var nickname = $('#user-nickname').val();

    stropheConnection.muc.join(roomName, nickname, function msg_handler_cb(messageXml, room) {
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
            stropheConnection: stropheConnection,
            room: room
          });
          
          return true;
        }
        
        var item = xElement.getElementsByTagName('item')[0];
        if (item) {
          $(document).trigger('strophe.muc.presense', {
            stropheConnection: stropheConnection,
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
  });
  
  $(document).on('strophe.muc.join', function (event, data) {
    var room = data.room;
    var stropheConnection = data.stropheConnection;
    
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
                stropheConnection: stropheConnection,
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
    if (data.jid == chatBotJid) {
	  chatBotNick = data.nick;
	} else {
      addParticipant(data.jid);
	}
  });
  
  $(document).on('strophe.muc.message', function (event, data) {
    var time = new Date();

    if (!data.delay) {
      showSlash(data.nick + ' said: ' + data.body);
    } else {
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
  
  $(document).on("click", '.illusion-group-participant .illusion-group-participant-image', function (event) {
	$(this).closest('.illusion-group-participant').find('.illusion-group-participant-menu').show();
  });
  
  $(document).on('illusion.menu.showCharacterSheet', function (event, data) {
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
  
}).call(this);