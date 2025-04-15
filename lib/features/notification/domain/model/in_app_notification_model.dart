class InAppNotification {
  final int id;
  final String title;
  final String body;
  final String? iconUrl;
  final String? resourceAction;
  final String? resourceId;
  final DateTime createdAt;

  InAppNotification({
    required this.id,
    required this.title,
    required this.body,
    this.iconUrl,
    this.resourceAction,
    this.resourceId,
    required this.createdAt,
  });

  static InAppNotification fromJson(notification) {
    return InAppNotification(
        id: notification['id'],
        title: notification['title'],
        body: notification['body'],
        iconUrl: notification['iconUrl'],
        resourceAction: notification['resourceAction'],
        resourceId:notification['resourceId'],
        createdAt: DateTime.parse(notification['createdAt'])
    );
  }
}