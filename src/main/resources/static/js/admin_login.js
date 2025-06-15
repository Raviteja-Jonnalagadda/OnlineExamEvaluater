$(document).ready(function() {
    $("#loginForm").submit(function(event) {
        event.preventDefault();
        
        var username = $("#username").val();
        var password = $("#password").val();

        if(username === "" || password === "") {
            $("#error-message").text("Please enter both username and password.");
            return;
        }

        $.ajax({
            method: "POST",
            url: "../login", // matches controller
            contentType: "application/json",
            data: JSON.stringify({ uname: username, pword: password }),
            success: function(response) {
                if(response === "success") {
                    window.location.href = "admin_dashboard.html";
                } else {
                    $("#error-message").text("Invalid username or password.");
                }
            },
            error: function(xhr, status, error) {
                console.error("Login error:", error);
                $("#error-message").text("Login failed. Try again.");
            }
        });
    });
});
