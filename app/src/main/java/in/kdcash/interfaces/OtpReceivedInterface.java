package in.kdcash.interfaces;


public interface OtpReceivedInterface {
  void onOtpReceived(String otp);
  void onOtpTimeout();
}
