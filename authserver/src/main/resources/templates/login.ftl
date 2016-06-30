<!DOCTYPE html>
<html lang="utf8">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="css/wro.css"/>
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
                <img class="logo-img" src="content/images/logo-jhipster.png"/>
                <span data-i18n="nav.appName">UAA</span>
            </a>
        </div>
        <div class="navbar-collapse collapse">
            <ul class="nav navbar-nav navbar-right">
                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true"
                       aria-expanded="false" data-i18n="nav.lang.title">Change Language <span class="caret"></span></a>
                    <ul class="dropdown-menu">
                        <li><a id="cnLangBut" href="#" data-i18n="nav.lang.zh_CN" data-value="zh_cn">中文</a></li>
                        <li><a id="enLangBut" href="#" data-i18n="nav.lang.en" data-value="en">英语</a></li>
                    </ul>
                </li>
            </ul>
        </div>
    </div>
</nav>

<div class="wrapper">
    <form class="form-signin" role="form" action="login" method="post">
        <h2 class="form-signin-heading">Please login</h2>
    <#if RequestParameters['error']??>
        <div class="alert alert-danger">
            There was a problem logging in. Please try again.
        </div>
    </#if>
        <input type="text" class="form-control" name="username" placeholder="Username" required="" autofocus=""/>
        <input type="password" class="form-control" name="password" placeholder="Password" required=""/>
        <label class="checkbox">
            <input type="checkbox" id="rememberMe" name="remember-me"> Remember me
        </label>
        <input type="hidden" id="csrf_token" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <button class="btn btn-lg btn-primary btn-block" type="submit">Login</button>
    </form>
</div>
<#--</div>-->
<script src="js/wro.js"></script>
</html>