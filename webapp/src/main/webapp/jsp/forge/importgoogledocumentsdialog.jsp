<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
  <head>
    <title>Dialog Content</title>
    <c:choose>
      <c:when test="${action eq 'list'}">
        <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/themes/default_dev/css/forge_embedded.css"/>
      </c:when>
      <c:when test="${action eq 'authenticate'}">
        <script type="text/javascript">
          function onWindowReady() {
            if (confirm("${warningMessage}")) { 
              parent.location.href='${authenticationUrl}';
            } else {
              getDialog().close();
            }
          };  
        </script>
      </c:when>
      <c:when test="${action eq 'close'}">
        <script type="text/javascript">
          function onWindowReady() {
            getDialog().close();
            parent.getWorkspaceController().reloadMaterialLists();
          };  
        </script>
      </c:when>
    </c:choose>
  </head>
  <body>
    <c:choose>
      <c:when test="${action eq 'list'}">  
        <div class="dialogContent importGoogleDocumentsDialogContent">
          <h3>
            <fmt:message key="forge.importGoogleDocuments.importDialogListTitle"/>
          </h3>

	        <form method="post">
	          <div class="stackedLayoutContainer">
	            <fieldset class="stackedLayoutFieldsContainer">     
	              <div class="importGoogleDocumentsEntries">
			            <c:if test="${rootFolder eq false}">
			              <div class="importGoogleDocumentsEntry ROOT_FOLDER">
			                <div class="importGoogleDocumentsEntrySelect"><input type="checkbox" disabled="disabled"/></div>
			                <div class="importGoogleDocumentsEntryIcon"></div>
			                <a class="importGoogleDocumentsEntryTitle" href="${pageContext.request.contextPath}/forge/importgoogledocumentsdialog.page">
			                  <fmt:message key="forge.importGoogleDocuments.rootFolderLabel"/>
			                </a>
			              </div>      
			            </c:if>
			            
			            <c:forEach var="file" items="${files}">
  		                <c:choose>
  		                  <c:when test="${file.mimeType eq 'application/vnd.google-apps.folder'}">
  		                    <div class="importGoogleDocumentsEntry FOLDER">
  		                      <div class="importGoogleDocumentsEntrySelect"><input type="checkbox" disabled="disabled"/></div>
  		                      <div class="importGoogleDocumentsEntryIcon"></div>
  		                      <a class="importGoogleDocumentsEntryTitle" href="${pageContext.request.contextPath}/forge/importgoogledocumentsdialog.page?folderId=${file.id}">${file.title}</a>
  		                    </div>   
  		                  </c:when>
  		                  <c:otherwise>
                        
                            <c:choose>
                              <c:when test="${file.mimeType eq 'application/vnd.google-apps.document'}">
                                <c:set var="icon" value="DOCUMENT"/>
                              </c:when>
                              
                              <c:when test="${file.mimeType eq 'application/vnd.google-apps.presentation'}">
                                <c:set var="icon" value="PRESENTATION"/>
                              </c:when>
                              
                              <c:when test="${file.mimeType eq 'application/vnd.google-apps.spreadsheet'}">
                                <c:set var="icon" value="SPREADSHEET"/>
                              </c:when>
                              
                              <c:when test="${file.mimeType eq 'application/vnd.google-apps.drawing'}">
                                <c:set var="icon" value="DRAWING"/>
                              </c:when>
                            </c:choose>
                        
  		                    <div class="importGoogleDocumentsEntry ${icon}">
  		                      <div class="importGoogleDocumentsEntrySelect"><input type="checkbox" id="entry-${file.id}" name="entryIds" value="${file.id}"/></div>
  		                      <div class="importGoogleDocumentsEntryIcon"></div>
  		                      <label for="entry-${file.id}" class="importGoogleDocumentsEntryTitle">${file.title}</label>
  		                    </div>            
  		                  </c:otherwise>
  		                </c:choose>
		              </c:forEach>
	              </div>
	            </fieldset>
	            <fieldset class="stackedLayoutButtonsContainer">
		            <jsp:include page="/jsp/templates/fragments/form_submitfield.jsp">
				          <jsp:param name="name" value="import" />
				          <jsp:param name="textLocale" value="forge.importGoogleDocuments.importButtonLabel" />
				          <jsp:param name="classes" value="formvalid formSaveButton" />
				        </jsp:include>
				        
				        <jsp:include page="/jsp/templates/fragments/form_submitfield.jsp">
                  <jsp:param name="name" value="cancel" />
                  <jsp:param name="textLocale" value="forge.importGoogleDocuments.cancelButtonLabel" />
                  <jsp:param name="classes" value="formvalid formCancelButton" />
                </jsp:include>
	            </fieldset>
	          </div>
	        </form>
        </div>
       
      </c:when>
      <c:otherwise>
        <p><fmt:message key="forge.importGoogleDocuments.waitMessage"/></p>
      </c:otherwise>
    </c:choose>
  </body>
</html>