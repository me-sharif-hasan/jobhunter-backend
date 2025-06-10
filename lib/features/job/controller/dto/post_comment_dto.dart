class PostCommentDto {
  String jobId;
  String comment;
  String parentUuid;

  PostCommentDto(
      {required this.jobId, required this.comment, required this.parentUuid});

  Map <String,dynamic> toCommentMap(){
    return {
      "jobId":jobId,
      "comment":comment,
      "parentUuid":parentUuid
    };
  }
}
