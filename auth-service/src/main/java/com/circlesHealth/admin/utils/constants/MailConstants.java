package com.circlesHealth.admin.utils.constants;

public class MailConstants {

    public static final String FORGOT_PASSWORD_SUBJECT = "Reset Password";
    public static final String MAIN_INSTRUCTIONS = "A request to reset your TruFishing account password has been made. If you did not make this request, simply ignore this email. If you did make this request, please reset your password:";
    public static final String REGARDS = "TruFishing Team";
    public static final String FROM_EMAIL = "testechtechment@gmail.com";
    public static final String FORGOTTEN_CODE_EMAIL_SUBJECT = "Retrieve Admin Access Code";
    public static final String FORGOTTEN_CODE_EMAIL = "Your Access Code is  : ";
    
    
    
    public static String getForgetPasswordMessage(String resetPasswordLink, String name) {
        return "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
                "<html xmlns=\"http://www.w3.org/1999/xhtml\" style=\"-ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%; height: 100%; width: 100%; margin: 0 auto; padding: 0;\">\n" +
                "<body width=\"100%\" style='-ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%; height: 100%; width: 100%; font-family: \"Helvetica Neue\", sans-serif; margin: 0 auto; padding: 0;' bgcolor=\"#f0f0f0\">\n" +
                "<table class=\"email-canvas  \" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" height=\"100%\" width=\"100%\" style=\"-ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%;  border-spacing: 0; border-collapse: collapse; margin: 0 auto;\" bgcolor=\"#f0f0f0\">\n" +
                "  <tr style=\"-ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%;\">\n" +
                "    <td valign=\"top\" style=\"-ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%; \">\n" +
                "      <center style=\"width: 100%; -ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%;\">\n" +
                "        <!--\n" +
                "            Set the email width. Defined in two places:\n" +
                "            1. max-width for all clients except Desktop Windows Outlook, allowing the email to squish on narrow but never go wider than 600px.\n" +
                "            2. MSO tags for Desktop Windows Outlook enforce a 600px width.\n" +
                "        -->\n" +
                "        <div class=\"email-wrapper\" style=\"-ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%; max-width: 600px; margin: auto;\">\n" +
                "          <!--[if (gte mso 9)|(IE)]>\n" +
                "          <table cellspacing=\"0\" cellpadding=\"0\" border=\"0\" width=\"600\" align=\"center\">\n" +
                "          <tr>\n" +
                "          <td>\n" +
                "          <![endif]-->\n" +
                "          <table class=\"email-header\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" align=\"center\" width=\"100%\" style=\"-ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%; border-spacing: 0; border-collapse: collapse; max-width: 600px; margin: 0 auto;\">\n" +
                "            <tr style=\"-ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%;\">\n" +
                "              <td style='-ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%;  font-family: \"Helvetica Neue\", sans-serif; color: #999; font-size: 13px; line-height: 1.6; padding: 20px 0;' align=\"center\">\n" +
                "                <a href=\"#\" style=\"-ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%; color: #757575; text-decoration: none;\"><img width=\"150\" id=\"headerImage\" alt=\"Trufishing\" src=\"https://trufishing-dev.s3.us-east-2.amazonaws.com/ic_logo.png\" style=\"-ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%; height: auto; line-height: 100%; outline: none; text-decoration: none; -ms-interpolation-mode: bicubic; max-width: 600px !important; border: 0;\"></a>\n" +
                "              </td>\n" +
                "            </tr>\n" +
                "          </table>\n" +
                "          <table class=\"email-body\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" align=\"center\" width=\"100%\" style=\"-ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%; border-spacing: 0; border-collapse: collapse; max-width: 600px; margin: 0 auto;\" bgcolor=\"#ffffff\">\n" +
                "            <tr style=\"-ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%;\">\n" +
                "              <td style=\"-ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%; \">\n" +
                "                <table border=\"0\" cellpadding=\"30\" cellspacing=\"0\" width=\"100%\" style=\"-ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%;  border-spacing: 0; border-collapse: collapse; margin: 0 auto;\">\n" +
                "                    <tr style=\"-ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%;\">\n" +
                "                      <td valign=\"top\" class=\"email-body-content\" style='-ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%;  font-family: \"Helvetica Neue\", sans-serif; color: #444; font-size: 14px; line-height: 150%;'>\n" +
                "                          <div id=\"greeting\" style=\"-ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%; margin-bottom: 20px; padding-bottom: 25px; border-bottom-width: 1px; border-bottom-color: #eee; border-bottom-style: solid;\">\n" +
                "                            <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"-ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%;  border-spacing: 0; border-collapse: collapse; margin: 0 auto;\">\n" +
                "                              <tr style=\"-ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%;\">\n" +
                "                                <td style=\"-ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%; \">\n" +
                "                                  <h1 class=\"greeting\" style='-ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%; font-family: \"Helvetica Neue\", sans-serif; color: #444; display: block; font-size: 18px; font-weight: 500; line-height: 1.3; margin: 0;'>\n" +
                "                                      Hi " + name + ",\n" +
                "                                    <span class=\"reason\" style=\"-ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%; display: block; font-size: 15px; font-weight: normal; color: #999;\">Here are your password reset instructions.</span>\n" +
                "                                  </h1>\n" +
                "                                </td>\n" +
                "                              </tr>\n" +
                "                            </table>\n" +
                "                          </div>\n" +
                "                          <p style=\"-ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%; margin-top: 15px; margin-bottom: 15px; font-size: 16px; line-height: 1.5; color: #555;\">" + MAIN_INSTRUCTIONS + "</p>\n" +
                "                        <table cellspacing=\"0\" cellpadding=\"0\" border=\"0\" align=\"center\" class=\"button-table \" style=\"-ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%; border-spacing: 0; border-collapse: collapse; margin: auto;\">\n" +
                "                           <tr style=\"-ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%;\">\n" +
                "                            <td class=\"button-td\" style=\"-ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%;  border-radius: 4px; -webkit-transition: all 100ms ease-in; transition: all 100ms ease-in;\" align=\"center\" bgcolor=\"#ea4c89\">\n" +
                "                              <a class=\"button-a\" href=\"" + resetPasswordLink + "\" style='-ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%; font-family: \"Helvetica Neue\", sans-serif; font-weight: 500; text-decoration: none; background-color: #ec6d50; font-size: 13px; line-height: 1.1; text-align: center; display: block; border-radius: 4px; -webkit-transition: all 100ms ease-in; transition: all 100ms ease-in; border: 15px solid #ec6d50;'>\n" +
                "                              <span style=\"-ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%; color: #ffffff;\">\n" +
                "                              &nbsp; &nbsp; Reset Password &nbsp; &nbsp; \n" +
                "                              </span>\n" +
                "                              </a>\n" +
                "                            </td>\n" +
                "                           </tr>\n" +
                "                        </table>\n" +
                "                        <p style=\"-ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%; margin-top: 15px; margin-bottom: 15px; font-size: 16px; line-height: 1.5; color: #555;\">Thank you,\n" +
                "                        <br style=\"-ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%;\">\n" +
                "                          " + REGARDS + "</p>\n" +
                "                      </td>\n" +
                "                    </tr>\n" +
                "                  </table>\n" +
                "                 </td>\n" +
                "                </tr>\n" +
                "             </table>\n" +
                "            <table class=\"email-footer\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" align=\"center\" width=\"100%\" style=\"-ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%;  border-spacing: 0; border-collapse: collapse; max-width: 600px; margin: 0 auto;\">\n" +
                "              <tr style=\"-ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%;\">\n" +
                "                <td style='-ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%;  font-family: \"Helvetica Neue\", sans-serif; color: #999; font-size: 13px; line-height: 1.6; padding: 30px 5%;' align=\"center\">\n" +
                "                  <div class=\"address\" style=\"-ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%; margin-top: 10px;\">\n" +
                "                    © Copyright 2020 TruFishing, Inc. All Rights Reserved.\n" +
                "                  </div>\n" +
                "                  </td>\n" +
                "              </tr>\n" +
                "            </table>\n" +
                "        </div>\n" +
                "      </center>\n" +
                "    </td>\n" +
                "  </tr>\n" +
                "</table>\n" +
                "</body>\n" +
                "</html>\n" +
                "\n";
    }

    private MailConstants() {
    }
}
