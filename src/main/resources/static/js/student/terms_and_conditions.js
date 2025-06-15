$(document).ready(function () {
  $('#agreeCheckbox').change(function () {
    $('#startExamBtn').prop('disabled', !this.checked);
  });

  $('#startExamBtn').click(function () {
    window.location.href = "exam_page.html";
  });
});
