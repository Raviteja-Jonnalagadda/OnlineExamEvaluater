$(document).ready(function () {
  // Load student name from localStorage or set default
  const username = localStorage.getItem("studentId");
  $('#studentName').text(username);

  // Update date & time
  function updateDateTime() {
    const now = new Date();
    $('#datetime').text(now.toLocaleString());
  }
  setInterval(updateDateTime, 1000);
  updateDateTime();

  // Slide cards animation
  $("#cardSlider").sortable({
    axis: "x",
    scroll: true
  });

  // On card click
  $(".dashboard-card").click(function () {
    const page = $(this).data("page");
    $("#contentFrame").attr("src", page);
  });
});
