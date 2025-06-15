$(document).ready(function () {
  const exam = JSON.parse(localStorage.getItem("examDetails"));
  const questions = [];

  $('#examIdDisplay').text(exam.examId);
  $('#examNameDisplay').text(exam.examName);
  $('#noOfQuestionsDisplay').text(exam.noOfQuestions);
  $('#durationDisplay').text(exam.duration);
  $('#totalMarksDisplay').text(exam.totalMarks);
  $('#passMarksDisplay').text(exam.passMarks);

  let questionCount = 0;

  $('#addQuestionBtn').click(function () {
    $('#questionSection').show();
    $('#optionInputs').hide(); // initially hide option area
    $('#questionId').val('');
    $('#questionText').val('');
    $('.optionInput').val('');
    $('input[name="correctOption"]').prop('checked', false);
  });

  $('#addOptionBtn').click(function () {
    $('#optionInputs').show();
    $('.optionInput').val('');
    $('input[name="correctOption"]').prop('checked', false);
  });

  $('#addToQueueBtn').click(function () {
    const qId = $('#questionId').val().trim();
    const qText = $('#questionText').val().trim();
    const correctOptionIndex = $('input[name="correctOption"]:checked').val();

    // Check if all required fields are filled
    if (!qId || !qText) {
      alert("Please fill in Question ID and Question text.");
      return;
    }

    const optionTexts = [];
    let emptyOption = false;
    for (let i = 0; i < 4; i++) {
      const optionVal = $(`#opt${i}`).val().trim();
      if (!optionVal) emptyOption = true;
      optionTexts.push(optionVal);
    }

    if (emptyOption) {
      alert("All four options must be filled.");
      return;
    }

    if (correctOptionIndex === undefined) {
      alert("Please select the correct option.");
      return;
    }

    // Check if questionId is unique
    const existingIds = $("#questionGrid").jqGrid('getDataIDs');
    for (let id of existingIds) {
      const rowData = $("#questionGrid").jqGrid('getRowData', id);
      if (rowData.questionId === qId) {
        alert("Question ID must be unique.");
        return;
      }
    }

    // Check max question count
    const exam = JSON.parse(localStorage.getItem("examDetails"));
    if (existingIds.length >= parseInt(exam.noOfQuestions)) {
      alert("Maximum number of questions reached.");
      return;
    }

    const correctAnswer = optionTexts[parseInt(correctOptionIndex)];

    const newRow = {
      questionId: qId,
      questionText: qText,
      options: optionTexts.join(', '),
      correctOption: correctAnswer
    };

    const newId = existingIds.length + 1;
    $("#questionGrid").jqGrid('addRowData', newId, newRow);

    // Add to local questions array (optional, depending on usage)
    questions.push(newRow);

    // Clear fields
    $('#questionId').val('');
    $('#questionText').val('');
    $('.optionInput').val('');
    $('input[name="correctOption"]').prop('checked', false);
    $('#optionInputs').hide();
    $('#questionSection').hide();

    if (existingIds.length + 1 >= parseInt(exam.noOfQuestions)) {
      $('#addQuestionBtn').prop('disabled', true);
    }
  });

  // Initialize jqGrid
  $("#questionGrid").jqGrid({
    datatype: "local",
    colNames: ['Question ID', 'Question', 'Options', 'Correct Option'],
    colModel: [
      { name: 'questionId', width: 80 },
      { name: 'questionText', width: 300 },
      { name: 'options', width: 200 },
      { name: 'correctOption', width: 100 }
    ],
    height: 'auto',
    pager: '#pager',
    viewrecords: true,
    caption: "Questions Preview"
  });

  $('#finalSubmitBtn').click(function () {
    const examData = {
      ...exam,
      questions: questions,
      makerId: 1001
    };

    localStorage.setItem("finalExamData", JSON.stringify(examData));
    window.location.href = 'confirm_exam.html';
  });
});
