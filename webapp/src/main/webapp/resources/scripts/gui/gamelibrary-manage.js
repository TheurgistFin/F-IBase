(function() {
  'use strict';

  $(document).ready(function () {
    $('.gamelibrary-manage-publication-image-link').magnificPopup({ 
      type: 'image'
    });
  });
  
  $(document).on('click', '.gamelibrary-manage-upload-image-link', function (event) {
    dust.render("gamelibrary-image-upload", {
      publicationId: $(this).data('publication-id')
    }, function(err, html) {
      if (!err) {
        var dialog = $(html);
        dialog.dialog({
          modal: true,
          width: 600,
          buttons: [{
            'text': dialog.data('upload-button'),
            'click': function(event) { 
              $(this).find('iframe').load($.proxy(function () {
                window.location.reload();
              }, this));
              
              $(this).find('form').submit();
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
  
  $(document).on('click', '.gamelibrary-manage-upload-file-link', function (event) {
    dust.render("gamelibrary-file-upload", {
      publicationId: $(this).data('publication-id')
    }, function(err, html) {
      if (!err) {
        var dialog = $(html);
        dialog.dialog({
          modal: true,
          width: 600,
          buttons: [{
            'text': dialog.data('upload-button'),
            'click': function(event) { 
              $(this).find('iframe').load($.proxy(function () {
                window.location.reload();
              }, this));
              
              $(this).find('form').submit();
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