class UserDataModel{
  final String email;
  final String name;
  final String? id;
  final String? photoUrl;
  UserDataModel({required this.email, required this.name,required this.id,this.photoUrl});
  UserDataModel.fromJson(Map<String, dynamic> json)
      : email = json['email'],
        name = json['name'],
        id = json['id'] != null
            ? json['id'] is num
            ? json['id'].toString()
            : json['id'].toString()
            : null,
        photoUrl = json['photoUrl']??json['imageUrl'];
  Map<String, dynamic> toJson() => {
    'email': email,
    'name': name,
    'id': id,
    'photoUrl': photoUrl,
  };
}