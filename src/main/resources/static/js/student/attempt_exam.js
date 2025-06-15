$(document).ready(function () {
  // Fetch exams from backend
  $.ajax({
    url: "/OnlineExamEvaluater/exams",
    method: "GET",
    success: function (exams) {
      exams.forEach(exam => {
        $('#examTable tbody').append(`
          <tr>
            <td><input type="radio" name="examRadio" value="${exam.examId}" data-name="${exam.examName}"></td>
            <td>${exam.examId}</td>
            <td>${exam.examName}</td>
          </tr>
        `);
      });
    },
    error: function () {
      alert("Failed to load exams.");
    }
  });

  // Enable proceed button on selection
  $(document).on('change', 'input[name="examRadio"]', function () {
    $('#proceedBtn').prop('disabled', false);
  });

  // Proceed button click
  $('#proceedBtn').click(function () {
    const selectedExam = $('input[name="examRadio"]:checked');
    const examId = selectedExam.val();
    const examName = selectedExam.data('name');
    localStorage.setItem("selectedExamId", examId);
    localStorage.setItem("selectedExamName", examName);
    window.location.href = "terms_and_conditions.html";
  });
});
