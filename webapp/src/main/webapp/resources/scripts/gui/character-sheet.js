(function() {
  'use strict';
  
  function getQueryParameters() {
    var result = {};
    
    var location = window.document.location;
    var search = location.search;
    if (search) {
    var params = search.substring(1).split('&');
      for (var i = 0, l = params.length; i < l; i++) {
        var param = params[i].split('=', 2);
        result[param[0]] = param[1];
      }
    }
    
    return result;
  }

  $.widget("custom.illusionField", {
    options: {
      sum: [],
    },
    _create : function() {
      if (this.options.sum.length > 0) {
        $(this.element).attr('readOnly', 'readOnly');

        $.each(this.options.sum, $.proxy(function (index, field) {
          $(field).change($.proxy(function (event) {
            this.updateSum();
          }, this));
        }, this));

        this.updateSum();
      } 
    },
    
    readOnly: function () {
      return $(this.element).attr('readOnly') == 'readOnly';
    },
    
    val: function (value) {
      if (value !== undefined) {
        $(this.element).val(value);
        $('.i-field-sum').illusionField('updateSum');
      } else {
        return $(this.element).val();
      }
    },
    
    updateSum: function () {
      var sum = 0;
      $.each(this.options.sum, $.proxy(function (index, field) {
        switch ($(field).attr('type')) {
          case 'number':
            sum += parseInt($(field).val());
          break;
          default:
            throw new Error("Can not sum of filed typed: " + $(field).attr('type'));
          break;
        }
      }, this));
      
      $(this.element).val(sum);
    }
  });
  
  $.widget("custom.illusionRoll", {
    _create : function() {
      this.element.click($.proxy(this._onClick, this));
    },

    fields: function() {
      var result = [];
      
      var fields = $(this.element).attr('data-fields');
      if (fields) {
        $.each(fields.split(','), function (index, field) {
          result.push(field);
        });
      }

      return result; 
    },

    fieldValues: function () {
      return $.map(this.fields(), function (field) {
        return parseInt($(field).val()||'0');
      });      
    },

    parseRoll: function (vars) {
      var roll = $(this.element).attr('data-roll');
      if (vars) {
        for (var i = 0, l = vars.length; i < l; i++) {
          roll = roll.replace(new RegExp('\\{' + i + '\\}', 'g'), vars[i]);
        }
      }
      return roll;
    },

    roll: function (vars) {
      var roll = this.parseRoll(vars||this.fieldValues());
      return [this.evalRoll(roll), roll];
    },

    evalRoll: function (roll) {
      var evil = eval;
      return evil('Math.round(' + roll
        .replace(/([0-9]{1,})([\*]{0,1})(d)([0-9]{1,})/g, "($1*(1 + (Math.random()*($4 - 1))))")
        .replace(/(d)([0-9]{1,})/g, "(1 + (Math.random()*($2 - 1)))") + ')');
    },
    
    _onClick: function () {
      var roll = this.roll();
      $(this.element).trigger("roll", {
        result: roll[0],
        roll: roll[1],
        label: $(this.element).attr('title')
      });
    }
  });
  
  $.widget("custom.illusionCharacterSheet", {
    options: {
      preview: false
    },
    _create : function() {
      // Initialize fields
      
      $(this.element).find('.i-field').each($.proxy(function (index, field) {
        if ($(field).hasClass('i-field-sum')) {
          $(field).illusionField({
            sum: $(field).attr('data-fields').split(',')
          });
        } else {
          $(field)
            .illusionField()
            .on('change', $.proxy(this._onIllusionFieldChange, this));   
        }
      }, this));    
      
      // Initialize progress bars
      
      $(this.element).find('.i-progressbar').each(function (index, field) {
        var targetField = $($(field).attr('data-field'));
        
        $(field).progressbar({
          value: parseInt(targetField.val())
        });

        targetField.change($.proxy(function (event) {
          $(this).progressbar("value", parseInt($(event.target).val()));
        }, field));
      });
      
      // Initialize rolls
      
      $(this.element).find('.i-roll').illusionRoll();
      
      if (!this.options.preview) {
        var socketUrl = (window.location.protocol == 'https:' ? 'wss:' : 'ws:') + '//' + window.location.host + this.options.contextPath + '/ws/' + this.options.eventId + '/characterSheet/' + this.options.materialId + '/' + this.options.participantId + '/' + this.options.key;
        this._webSocket = this._openWebSocket(socketUrl);
        this._webSocket.onmessage = $.proxy(this._onWebSocketMessage, this);
        this._webSocket.onopen = $.proxy(this._onWebSocketOpen, this);
        $(window).on('beforeunload', $.proxy(this._onWindowBeforeUnload, this));
      }
      
      $(this.element).on('roll', '.i-roll', $.proxy(this._onRoll, this));
    },
    
    load: function (sheetData) {
      for (var key in sheetData) {
        var value = sheetData[key];
        $('*[name="' + key + '"]').illusionField('val', value);
      }

      if ($(this.element).find('.i-data-link').length > 0) {
        this._updateDataLinks(sheetData);
      }
      
      
    },
    
    set: function (key, value) {
      $('*[name="' + key + '"]').illusionField('val', value);
    },
    
    _sendUpdate: function (key, value) {
      if (!this.options.preview) {
        this._webSocket.send(JSON.stringify({
          type: 'update',
          data: { 
            key: key, 
            value: value 
          }
        }));
      }
      
      if ($(this.element).find('.i-data-link').length > 0) {
        var sheetData = {};
        $(this.element).find('.i-field').each(function (index, field) {
          if ($(field).illusionField('readOnly')) {
            sheetData[$(field).attr('name')] = $(field).val();
          }
        });
        
        this._updateDataLinks(sheetData);
      }
    },
    
    _sendRoll: function (label, roll, result) {
      if (!this.options.preview) {
        this._webSocket.send(JSON.stringify({
          type: 'roll',
          data: { 
            label: label, 
            roll: roll,
            result: result
          }
        }));
      }
    },
    
    _updateDataLinks: function (sheetData) {
      $(this.element).find('.i-data-link').attr('href', window.document.location.search + '&d=' + btoa(JSON.stringify(sheetData)));
    },
    
    _openWebSocket: function (url) {
      if ((typeof window.WebSocket) !== 'undefined') {
        return new WebSocket(url);
      } else if ((typeof window.MozWebSocket) !== 'undefined') {
        return new MozWebSocket(url);
      }
      
      return null;
    },
    
    _onWebSocketMessage: function (event) {
      var data = event.data;
      var message = $.parseJSON(data);
      var messageData = $.parseJSON(message.data);
      switch (message.type) {
        case 'load':
          this.load(messageData.values);
        break;
        case 'update':
          this.set(messageData.key, messageData.value);
        break;
      }
    },
    
    _onWebSocketOpen: function (event) {
      this._webSocket.onclose = $.proxy(this._onWebSocketClose, this);    
      this._webSocket.onerror = $.proxy(this._onWebSocketError, this);    
    },
    
    _onWebSocketClose: function (event) {
      if (event.code == 1000) {
        window.location.reload(true);
      } else {
        alert('Communication error with server, please try again later');
      }
    },
    
    _onWebSocketError: function (event) {
      alert('Communication error with server, please try again later');
    },
    
    _onIllusionFieldChange: function (event, data) {
      this._sendUpdate($(event.target).attr('name'), $(event.target).val());
    },
    
    _onWindowBeforeUnload: function (event) {
      this._webSocket.onclose = function () {};
      this._webSocket.close();
    },
    
    _onRoll: function (event, data) {
      this._sendRoll(data.label, data.roll, data.result);
    }
  });
  
  $(document).ready(function () {
    var params = getQueryParameters();

    $(document.body).illusionCharacterSheet({
      preview: (typeof PREVIEW != 'undefined') ? PREVIEW : false,
      contextPath: params.contextPath,
      participantId: params.participantId,
      eventId: params.eventId,
      materialId: params.materialId,
      key: params.key
    });
    
    if (params['d']) {
      if (window.atob) {
        var data = atob(params['d']);
        if (data) {
          $(document.body).illusionCharacterSheet('data', $.parseJSON(data));
        }
      }
    }
  }); 
  
}).call(this);