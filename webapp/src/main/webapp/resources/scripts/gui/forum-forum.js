(function() {
  'use strict';
  $(document).ready(function() {
    $('.forum-view-new-topic-link').click(function (e) {
      $(this).hide();
      
      CKEDITOR.replaceClass = 'forum-view-new-topic-contents-editor';
      CKEDITOR.replaceAll( function( textarea, config ) {
        config.toolbar = [
          { name: 'clipboard',   items : [ 'Cut','Copy','Paste','-','Undo','Redo' ] },
          { name: 'editing',     items : [ 'Find','Replace','-','SelectAll','-','Scayt' ] },
          { name: 'basicstyles', items : [ 'Bold','Italic','Underline','Strike','Subscript','Superscript','-','RemoveFormat' ] },
          { name: 'paragraph',   items : [ 'NumberedList','BulletedList','-','Outdent','Indent','-','Blockquote' ] },
          { name: 'links',       items : [ 'Link','Unlink' ] },
          { name: 'insert',      items : [ 'Image','SpecialChar' ] },
          { name: 'tools',       items : [ 'Maximize' ] }
        ];
        
        config.contentsCss = ['//cdnjs.cloudflare.com/ajax/libs/ckeditor/4.2/contents.css', CONTEXTPATH + '/forum/ckcontents.css' ];
      });
      
      $('.forum-view-new-topic-editor-container').show();
    });
  });
}).call(this);