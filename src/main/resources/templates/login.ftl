<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>用户登录 - ByYH</title>

    <link href="/css/bootstrap.min.css" rel="stylesheet">
    <link href="/css/signin.css" rel="stylesheet">
    <script src="/js/jquery-3.6.0.min.js"></script>
</head>
<body>

<body class="text-center">

<main class="form-signin">
    <form>
        <h1 class="h3 mb-3 fw-normal">阅读收藏 - ByYH</h1>

        <img src="/images/wryhysg.jpg" alt="公众号：微如萤火亦是光">
        <div class="mt-2 mb-2 text-muted">
            登录验证码：
            <strong style="background-color: yellow; padding: 2px; font-size: 18px;" id="qrcodeeeee">
                ${code}
            </strong>
        </div>
        <p class="text-muted">扫码关注公众号，回复上方验证码登录</p>
    </form>
</main>

<script>

    var dingshi = setInterval(function () {
        $.get('/login-check', {
            code: '${code}',
            ticket: '${ticket}'
        }, function (res) {
            console.log(res)
            if (res.code === 0) {
                location.href = "/";
            }
        })
    }, 3000);

    setTimeout(function () {
        clearInterval(dingshi);
        $("#qrcodeeeee").text("验证码过期，请刷新！");
    }, 180000)

</script>

</body>

</body>
</html>