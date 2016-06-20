<html>
<head>
    <link rel="stylesheet" href="css/wro.css"/>
</head>
<body>
    <div class="wrapper">
        <form class="form-signin" role="form" action="login" method="post">
            <h2 class="form-signin-heading">Please login</h2>
            <#if RequestParameters['error']??>
                <div class="alert alert-danger">
                    There was a problem logging in. Please try again.
                </div>
            </#if>
            <input type="text" class="form-control" name="username" placeholder="Username" required="" autofocus="" />
            <input type="password" class="form-control" name="password" placeholder="Password" required=""/>
            <label class="checkbox">
                <input type="checkbox" id="rememberMe" name="remember-me"> Remember me
            </label>
            <input type="hidden" id="csrf_token" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <button class="btn btn-lg btn-primary btn-block" type="submit">Login</button>
        </form>
    </div>
<#--</div>-->
</html>