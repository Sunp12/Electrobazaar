<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <title>Register Page</title>
        <link
            href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
            rel="stylesheet"
            integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
            crossorigin="anonymous"
            />
        <link rel="stylesheet" href="style.css" />

    </head>
    <body>
        <jsp:include page="header.jsp"></jsp:include>
            <div class="container m-5">
                <div class="row justify-content-center">
                    <form action="./RegisterServlet" class="col-md-6 myform">
                        <div class="text-center mt-3">
                            <h2 class="text-primary">Registration Form</h2>
                        </div>

                        <div class="row">
                            <div class="col-md-6 form-group">
                                <label for="user_name">Name</label>
                                <input type="text" class="form-control" id="user_name" name="user_name" required />
                            </div>

                            <div class="col-md-6 form-group ">
                                <label for="user_email">Email</label>
                                <input type="email" class="form-control" id="user_email" name="user_email" required />
                                <button type="button" class="btn btn-secondary mt-2" onclick="sendMailOtp()">Send OTP</button>

                                <div class="d-flex align-items-center mt-2">

                                    <input type="text" class="form-control me-2" id="email_otp" placeholder="Enter OTP" required />
                                    <button type="button" class="btn btn-primary" onclick="verifyEmailOtp()">Verify</button>
                                </div>
                            </div>


                            <div class="form-group mt-3">
                                <label for="address">Address</label>
                                <textarea id="address" class="form-control" name="address" required></textarea>
                            </div>

                            <div class="row mt-3">
                                <div class="col-md-6 form-group">
                                    <label for="mobile">Mobile</label>
                                    <input type="text" class="form-control" id="mobile" name="mobile" required />
                                  
                                </div>


                                <div class="col-md-6 form-group">
                                    <label for="pincode">Pincode</label>
                                    <input type="number" class="form-control" id="pincode" name="pincode" required/>
                                </div>
                            </div>

                            <div class="row mt-3">
                                <div class="col-md-6 form-group">
                                    <label for="password">Password</label>
                                    <input type="password" class="form-control" id="password" name="password" required />
                                </div>

                                <div class="col-md-6 form-group">
                                    <label for="cpassword">Confirm Password</label>
                                    <input type="password" class="form-control" id="cpassword" name="cpassword" required />
                                </div>
                            </div>
                            <div class="row mt-3">
                                <div class="col-md-12 form-group">
                                    <label for="loginas">Register As</label>
                                    <select name="userType" id="loginas" class="form-control">
                                        <option value="customer">Customer</option>
                                        <option value="admin">Admin</option>
                                    </select>
                                </div>
                            </div>

                            <div class="row mt-4 text-center">
                                <div class="col-md-6">
                                    <button type="reset" class="btn btn-danger">Reset</button>
                                </div>
                                <div class="col-md-6 " >
                                    <button style="display: none;" type="submit" class="btn btn-primary" id="sBtn">Register</button>
                                </div>
                            </div>
                    </form>
                </div>
            </div>
        <jsp:include page="footer.jsp"></jsp:include>
        <script
            src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
            crossorigin="anonymous"
        ></script>
        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>

        <script>
                                        function sendMailOtp() {
                                            var email = $("#user_email").val();

                                            if (email) {
                                                $.post("RegisterServlet", {action: "generateOtp", user_email: email}, function (response) {
                                                    alert(response);
                                                });
                                            } else {
                                                alert("Please enter a valid email");
                                            }
                                        }


                                        function verifyEmailOtp() {
                                            var email = $("#user_email").val();

                                            var otp = $("#email_otp").val();
                                            if (otp && email) {
                                                $.post("RegisterServlet", {action: "verifyOtp", user_email: email, otp: otp}, function (response) {
                                                    if (response === "success") {
                                                        let btn = document.getElementById("sBtn")
                                                        btn.style.display = "block";
                                                        $("#user_email, #email_otp").prop("readonly", true); // Lock email fields
                                                        alert("OTP Verified Successfully!");
                                                    } else {
                                                        alert("Invalid OTP, try again.");
                                                    }
                                                });
                                            } else {
                                                alert("Enter the OTP received.");
                                            }
                                        }
                                       
        </script>


    </body>
</html>
