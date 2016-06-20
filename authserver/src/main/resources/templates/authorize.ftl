<html>
<head>
    <link rel="stylesheet" href="../css/wro.css"/>
</head>
<body>
<div class="wrapper">
    <div class="form-confirm">
        <h2>Please Confirm</h2>

        <p>
            Do you authorize "${authorizationRequest.clientId}" at "${authorizationRequest.redirectUri}" to access your
            protected resources
            with scope ${authorizationRequest.scope?join(", ")}.
        </p>
        <form id="confirmationForm" name="confirmationForm"
              action="../oauth/authorize" method="post">
            <input name="user_oauth_approval" value="true" type="hidden"/>

            <#--<ul class="list-unstyled">-->
                <#--<c:forEach items="${scopes}" var="scope">-->
                    <#--<c:set var="approved">-->
                        <#--<c:if test="${scope.value}"> checked</c:if>-->
                    <#--</c:set>-->
                    <#--<c:set var="denied">-->
                        <#--<c:if test="${!scope.value}"> checked</c:if>-->
                    <#--</c:set>-->
                    <#--<li>-->
                        <#--<div class="form-group">-->
                        <#--${scope.key}: <input type="radio" name="${scope.key}"-->
                                             <#--value="true" ${approved}>Approve</input> <input type="radio"-->
                                                                                             <#--name="${scope.key}" value="false" ${denied}>Deny</input>-->
                        <#--</div>-->
                    <#--</li>-->
                <#--</c:forEach>-->
            <#--</ul>-->

            <input type="hidden" id="csrf_token" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <button class="btn btn-primary btn-block" type="submit">Approve</button>
        </form>
        <form id="denyForm" name="confirmationForm"
              action="../oauth/authorize" method="post">
            <input name="user_oauth_approval" value="false" type="hidden"/>
            <input type="hidden" id="csrf_token" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <button class="btn btn-warning btn-block" type="submit">Deny</button>
        </form>
    </div>
</div>
</html>