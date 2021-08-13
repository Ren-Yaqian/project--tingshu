window.addEventListener("load", function () {
  var olElement = document.querySelector("ol");

  // 1. 判断用户是否有登录
  ajax("get", "/api/current-user.json", function (result) {
    if (!result.logged) {
      alert("这个页面要求必须先登录后才能使用，即将跳转到登录页")
      window.location = "/login.html";
      return;
    }

    // 2. 拉取我的专辑列表
    ajax("get", "/api/my-album-list.json", function (result) {
      if (!result.success) {
        alert("获取资源失败。" + result.reason);
        return;
      }

      var albumList = result.data;
      for (var album of albumList) {
        var html = `<li><a href="/album-editor.html?aid=${album.aid}">${album.name}</a></li>`
        olElement.innerHTML += html;
      }
    })
  })
})