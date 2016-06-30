<!DOCTYPE html>
<html lang="utf8">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="../css/wro.css"/>
</head>
<body>
<nav class="navbar navbar-default" role="navigation">
    <div class="container">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand logo">
                <img class="logo-img" src="../content/images/logo-jhipster.png"/>
                <span data-i18n="nav.appName">UAA</span>
            </a>
        </div>
        <div class="navbar-collapse collapse">
            <ul class="nav navbar-nav navbar-right">
                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true"
                       aria-expanded="false" data-i18n="nav.lang.title">Change Language <span class="caret"></span></a>
                    <ul class="dropdown-menu">
                        <li><a id="cnLangBut" data-i18n="nav.lang.zh_CN" data-value="zh_cn">中文</a></li>
                        <li><a id="enLangBut" data-i18n="nav.lang.en" data-value="en">英语</a></li>
                    </ul>
                </li>
            </ul>
        </div>
    </div>
</nav>
<div class="wrapper">
    <div class="form-confirm">
        <h2 data-i18n="confirm.title">Please Confirm</h2>

        <p>
            <span data-i18n="confirm.tip.client">Client: </span> ${client_id}
            <span data-i18n="confirm.tip.url"> at </span>${redirect_uri}
            <span data-i18n="confirm.tip.resource">to access your protected resources.</span>
        </p>
        <form id="confirmationForm" name="confirmationForm"
              action="../oauth/authorize" method="post">
        <#--<input name="user_oauth_approval" value="true" type="hidden"/>-->

            <#if scopes?exists>
                <#list scopes?keys as scope>
                    <div class="form-group">
                        <input class="checkbox-inline" type="checkbox" name="${scope}" value="true" disabled checked>
                        <span data-i18n="confirm.resources.allow">allow</span>${scope}.
                    </div>
                </#list>
            </#if>

            <input type="hidden" id="csrf_token" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <button class="btn btn-primary btn-block" type="submit" name="user_oauth_approval"
                    value="true" data-i18n="confirm.resources.approve">Approve
            </button>
            <button class="btn btn-warning btn-block" type="submit" data-i18n="confirm.resources.deny">Deny</button>
        </form>
    </div>
</div>

<script src="../js/wro.js"></script>

</html>