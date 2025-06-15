$(document).ready(function () {
    const examId = localStorage.getItem("selectedExamId");
    const examName = localStorage.getItem("selectedExamName");
    const studentId = localStorage.getItem("studentId");
	
    if (!examId || !examName || !studentId) {
        alert("Missing exam or student details.");
        return;
    }

    $('#examName').text(examName);

    // Fetch exam duration and start timer
    $.ajax({
        url: `/OnlineExamEvaluater/examdetails?examId=${examId}`,
        method: 'GET',
        success: function (examDetails) {
            const examName = examDetails.examName;
            const timeDuration = examDetails.timeDuration;

            if (!timeDuration) {
                alert("Time duration missing from exam details.");
                return;
            }

            $('#examName').text(examName);
            startTimer(timeDuration);
            loadQuestions(); // Fetch and display questions
        },
        error: function () {
            alert("Failed to fetch exam details.");
        }
    });

    function startTimer(timeDuration) {
        let totalSeconds = parseInt(timeDuration) * 60;
        const timerInterval = setInterval(() => {
            totalSeconds--;
            const minutes = Math.floor(totalSeconds / 60);
            const seconds = totalSeconds % 60;
            $('#timeLeft').text(`${minutes}m ${seconds}s`);

            if (totalSeconds <= 0) {
                clearInterval(timerInterval);
                submitExam();
            }
        }, 1000);
    }

    function loadQuestions() {
        $.ajax({
            url: `/OnlineExamEvaluater/questions?examId=${examId}`,
            method: 'GET',
            success: function (questions) {
                questions.forEach((q, index) => {
                    const questionHtml = `
                        <div class="question" data-question-id="${q.questionId}">
                            <p><strong>Q${index + 1}:</strong> ${q.question}</p>
                            <div class="options">
                                <label><input type="radio" name="q${q.questionId}" value="option1"> ${q.option1}</label>
                                <label><input type="radio" name="q${q.questionId}" value="option2"> ${q.option2}</label>
                                <label><input type="radio" name="q${q.questionId}" value="option3"> ${q.option3}</label>
                                <label><input type="radio" name="q${q.questionId}" value="option4"> ${q.option4}</label>
                            </div>
                        </div>
                    `;
                    $('#questionsContainer').append(questionHtml);
                });
            },
            error: function () {
                alert("Failed to load questions.");
            }
        });
    }

    $('#examForm').on('submit', function (e) {
        e.preventDefault();
        submitExam();
    });

    function submitExam() {
        const answers = {};
        $('#questionsContainer .question').each(function () {
            const questionId = $(this).data('question-id');
            const selectedInput = $(this).find('input[type=radio]:checked');
            const selectedOptionText = selectedInput.length > 0 ? selectedInput.parent().text().trim() : "NA";
            answers[questionId] = selectedOptionText;
        });

        // Send answers to backend
        $.ajax({
            url: '/OnlineExamEvaluater/submit',
            method: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({
                examId: examId,
                studentId: studentId,
                answers: answers
            }),
            success: function (response) {
                if (response && response.resultStatus && response.obtainedMarks !== undefined && response.totalMarks !== undefined) {
                    // Store result temporarily
                    localStorage.setItem("lastExamScore", response.obtainedMarks);
                    localStorage.setItem("lastExamTotal", response.totalMarks);
                    localStorage.setItem("lastExamResult", response.resultStatus);
                    window.location.href = "exam_attempt_complete.html";
                } else {
                    alert("Exam submitted, but response is incomplete.");
                }
            },
            error: function () {
                alert("Failed to submit exam.");
            }
        });
    }
});
