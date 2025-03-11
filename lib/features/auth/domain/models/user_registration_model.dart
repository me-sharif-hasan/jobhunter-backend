class UserRegistrationModel{
  final String email;
  String password;
  final String name;
  String confirmPassword;
  String token;
  String imageUrl;

  UserRegistrationModel({required this.email,
    this.password="",
    this.confirmPassword="",
    required this.name,
    this.token="",
    this.imageUrl=""
  });

  Map <String,dynamic> toJson(){
    return {
      "email":email,
      "password":password,
      "name":name,
      "confirmPassword":confirmPassword,
      "token":token,
      "imageUrl":imageUrl
    };
  }
}