$(document).ready(function () {
    const studentId = localStorage.getItem("studentId");

    if (!studentId) {
        alert("Student ID not found. Please login again.");
        return;
    }

    $.ajax({
        url: "/OnlineExamEvaluater/getProfile",
        method: "GET",
        data: { studentId: studentId },
        success: function (profile) {
			console.log(profile);
            $("#studentId").text(profile.STUDENT_ID || '');
            $("#fullName").text(profile.FULL_NAME || '');
            $("#email").text(profile.EMAIL || '');
            $("#phone").text(profile.PHONE_NUMBER || '');
            $("#dob").text(profile.DATE_OF_BIRTH || '');
            $("#registeredOn").text(profile.REGISTERED_ON || '');
            $("#course").text(profile.COURSE || '');
            $("#college").text(profile.COLLEGE || '');
            $("#year").text(profile.YEAR || '');
        },
        error: function () {
            alert("Failed to fetch profile. Try again later.");
        }
    });
});
