$(document).ready(function () {
  $('#examId').on('blur', function () {
    const examId = $(this).val();
    if (examId.trim() !== '') {
      $.ajax({
        url: '../checkExamId/' + examId,
        type: 'GET',
        success: function (response) {
          if (response === 'exists') {
            $('#idStatus').text('Exam ID already exists!');
            $('#addQuestionsBtn').prop('disabled', true);
          } else {
            $('#idStatus').text('');
            $('#addQuestionsBtn').prop('disabled', false);
          }
        },
        error: function () {
          $('#idStatus').text('Error checking Exam ID');
        }
      });
    }
  });

  $('#addQuestionsBtn').click(function () {
    const exam = {
      examId: $('#examId').val(),
      examName: $('#examName').val(),
      noOfQuestions: $('#noOfQuestions').val(),
      duration: $('#duration').val(),
      totalMarks: $('#totalMarks').val(),
      passMarks: $('#passMarks').val()
    };

    localStorage.setItem('examDetails', JSON.stringify(exam));
    window.location.href = 'add_questions.html';
  });
});
