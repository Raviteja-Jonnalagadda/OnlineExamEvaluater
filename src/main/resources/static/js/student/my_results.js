$(document).ready(function () {
  const studentId = localStorage.getItem("studentId");

  $.ajax({
    url: "/OnlineExamEvaluater/studentResults",
    method: "GET",
    data: { studentId },
    success: function (data) {
      let container = $("#resultsContainer");
      container.empty();

      data.forEach(result => {
        let card = $("<div>").addClass("result-card");
        card.addClass(result.resultStatus === "Y" ? "pass" : "fail");

        card.append(`<p><strong>Exam ID:</strong> ${result.examId}</p>`);
        card.append(`<p><strong>Result ID:</strong> ${result.RESULT_ID}</p>`);
        card.append(`<p><strong>Marks Obtained:</strong> ${result.obtainedMarks}</p>`);
        card.append(`<p><strong>Status:</strong> ${result.resultStatus === 'Y' ? 'PASS' : 'FAIL'}</p>`);
        card.append(`<p><strong>Date:</strong> ${result.resultDate}</p>`);

        const downloadBtn = $("<button>")
          .text("Download Result (JPEG)")
          .addClass("download-btn")
          .on("click", function () {
            window.open(`/OnlineExamEvaluater/downloadresults?type=onerec&resultId=${result.RESULT_ID}`);
          });

        card.append(downloadBtn);
        container.append(card);
      });
    },
    error: function () {
      alert("Failed to load results.");
    }
  });

  $("#downloadAllBtn").click(function () {
    window.open("/OnlineExamEvaluater/downloadresults?type=full&studentId=" + studentId);
  });
});
