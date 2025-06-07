import '../../../auth/domain/models/user_data_model.dart';

class JobCommentModel {
  final UserDataModel user;
  final String uuid;
  final String comment;
  final String jobId;
  final int userId;
  final String? parentUuid;
  final DateTime createTime;
  final DateTime updateTime;

  JobCommentModel({
    required this.user,
    required this.uuid,
    required this.comment,
    required this.jobId,
    required this.userId,
    this.parentUuid,
    required this.createTime,
    required this.updateTime,
  });

  factory JobCommentModel.fromJson(Map<String, dynamic> json) {
    return JobCommentModel(
      user: UserDataModel.fromJson(json['user']),
      uuid: json['uuid'],
      comment: json['comment'],
      jobId: json['jobId'],
      userId: json['userId'],
      parentUuid: json['parentUuid'],
      createTime: DateTime.parse(json['createTime']),
      updateTime: DateTime.parse(json['updateTime']),
    );
  }
}
