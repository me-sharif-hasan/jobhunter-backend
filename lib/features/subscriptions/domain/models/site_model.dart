import 'dart:developer';

class Site {
  final int id;
  final String name;
  final String homepage;
  final String description;
  final String iconUrl;
  final String jobListPageUrl;
  final DateTime lastCrawledAt;
  bool subscribed;

  Site({
    required this.id,
    required this.name,
    required this.homepage,
    required this.description,
    required this.iconUrl,
    required this.jobListPageUrl,
    required this.lastCrawledAt,
    this.subscribed = false,
  });

  factory Site.fromJson(Map<String, dynamic> json) {
    log('iijson:'+json.toString());
    return Site(
      id: json['id'] as int,
      name: json['name'] as String,
      homepage: json['homepage'] as String,
      description: json['description'] as String,
      iconUrl: json['iconUrl'] as String,
      jobListPageUrl: json['jobListPageUrl'] as String,
      lastCrawledAt: DateTime.parse(json['lastCrawledAt'] as String),
      subscribed: json['subscribed'] as bool,
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'name': name,
      'homepage': homepage,
      'description': description,
      'iconUrl': iconUrl,
      'jobListPageUrl': jobListPageUrl,
      'lastCrawledAt': lastCrawledAt.toIso8601String(),
      'subscribed': subscribed,
    };
  }

  Site copyWith({String? jobListPageUrl}) {
    return Site(
      id: id,
      name: name,
      homepage: homepage,
      description: description,
      iconUrl: iconUrl,
      jobListPageUrl: jobListPageUrl ?? this.jobListPageUrl,
      lastCrawledAt: lastCrawledAt,
      subscribed: subscribed,
    );
  }

  get constructedIcon{
    return "https://www.google.com/s2/favicons?domain=${Uri.parse(homepage!).host}&sz=64";
    }
}