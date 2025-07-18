class ResumeStrengthModel {
  final int score;
  final String reasoning;

  ResumeStrengthModel({
    required this.score,
    required this.reasoning,
  });

  factory ResumeStrengthModel.fromJson(Map<String, dynamic> json) {
    return ResumeStrengthModel(
      score: json['score'] ?? 0,
      reasoning: json['reasoning'] ?? '',
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'score': score,
      'reasoning': reasoning,
    };
  }
}
