$(document).ready(function () {
  // Generate captcha
  let a = Math.floor(Math.random() * 10 + 1);
  let b = Math.floor(Math.random() * 10 + 1);
  $('#captchaQuestion').text(`What is ${a} + ${b} ?`);

  $('#studentLoginForm').submit(function (e) {
    e.preventDefault();

    const answer = parseInt($('#captchaAnswer').val());
    if (answer !== a + b) {
      $('#error').text('Captcha answer is incorrect.');
      return;
    }

    // ✅ Save studentId to localStorage
    localStorage.setItem("studentId", $('#username').val());

    $.ajax({
      url: '/OnlineExamEvaluater/studentlogin',
      method: 'POST',
      contentType: 'application/json',
      data: JSON.stringify({
        username: $('#username').val(),
        password: $('#password').val()
      }),
      success: function (res) {
        // res is a plain string like "success" or "fail"
        //alert("Server response: " + res);
        if (res.trim() === 'success') {
          //alert("Login successful!");
          window.location.href = 'student_dashboard.html';
        } else {
          $('#error').text('Invalid username or password');
        }
      },
      error: function (xhr, status, error) {
        console.error("AJAX error:", status, error);
        $('#error').text('Server error occurred.');
      }
    });
  });
});
