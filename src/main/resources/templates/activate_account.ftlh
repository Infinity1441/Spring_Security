<#--<!DOCTYPE html>-->
<#--<html lang="en">-->
<#--<head>-->
<#--    <meta charset="UTF-8">-->
<#--    <title>Title</title>-->
<#--</head>-->
<#--<body style="background-color: magenta;margin: 0; padding: 0;">-->
<#--<h1 style="text-align: center; color: green">Hi ${username}</h1>-->
<#--<h3>You have successfully registered!!!</h3>-->
<#--<h3>In order to Activate your account please click link below !!!</h3>-->
<#--<a href="${url}" target="_blank">Activate</a>-->
<#--</body>-->
<#--</html>-->

<!DOCTYPE html>
<html>
<head>
    <title>OTP Verification</title>
    <style>
        body {
            font-family: Arial, sans-serif;
        }
        .container {
            max-width: 400px;
            margin: 0 auto;
            padding: 20px;
            border: 1px solid #ccc;
            border-radius: 5px;
        }
        .form-group {
            margin-bottom: 15px;
        }
        .form-group button {
            background-color: #4CAF50;
            color: white;
            padding: 10px 20px;
            border: none;
            border-radius: 3px;
            cursor: pointer;
        }
    </style>
</head>
<body>
<div class="container">
    <h2>Hello ${username}! This is your verification code. Please click the button below to activate your account!</h2>
    <form method="post" action="/verify-otp">
        <div class="form-group">
            <button type="button"><a href="${url}" target="_blank" style="font-size: 16px;color: white;text-decoration: none">Activate</a></button>
        </div>
    </form>
</div>
</body>
</html>