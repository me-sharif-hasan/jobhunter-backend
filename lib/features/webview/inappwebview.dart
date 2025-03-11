import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:flutter_inappwebview/flutter_inappwebview.dart';
import 'package:personalized_job_hunter/features/common/controller/meta_controller.dart';
import 'package:personalized_job_hunter/util/values/constants.dart';
import 'package:provider/provider.dart';
import 'package:url_launcher/url_launcher.dart';

import '../common/domain/model/job_model.dart';
import '../common/widgets/modern_loader.dart';

class JobWebViewScreen extends StatefulWidget {
  Job? job;
  final String url;
  String title = 'Job Details';
  String? company;

  JobWebViewScreen({super.key, required this.job, required this.url});
  JobWebViewScreen.manual({super.key, required this.title, required this.company, required this.url});

  @override
  State<JobWebViewScreen> createState() => _JobWebViewScreenState();
}

class _JobWebViewScreenState extends State<JobWebViewScreen> {
  InAppWebViewController? _webViewController;
  bool _isLoading = true;
  double _progress = 0;

  @override
  void initState() {
    super.initState();
    // Enable WebView debugging on Android in debug mode
    if (!kIsWeb && defaultTargetPlatform == TargetPlatform.android) {
      InAppWebViewController.setWebContentsDebuggingEnabled(true);
    }
  }

  @override
  Widget build(BuildContext context) {
    final url = widget.url;

    return Scaffold(
      body: Consumer<MetaController>(
        builder: (context, controller, _) {
          return Container(
            decoration: BoxDecoration(
              gradient: LinearGradient(
                begin: Alignment.topLeft,
                end: Alignment.bottomRight,
                colors: [
                  Color(Constants.themeColor[controller.currentPage][0]).withOpacity(0.95),
                  Color(Constants.themeColor[controller.currentPage][1]).withOpacity(0.95),
                ],
                stops: const [0.0, 1.0],
              ),
            ),
            child: SafeArea(
              child: Column(
                children: [
                  // Custom modern header
                  Container(
                    padding: const EdgeInsets.symmetric(horizontal: 16.0, vertical: 12.0),
                    decoration: BoxDecoration(
                      gradient: LinearGradient(
                        begin: Alignment.topLeft,
                        end: Alignment.bottomRight,
                        colors: [
                          Color(Constants.themeColor[controller.currentPage][0]).withOpacity(0.95),
                          Color(Constants.themeColor[controller.currentPage][1]).withOpacity(0.95),
                        ],
                      ),
                      boxShadow: [
                        BoxShadow(
                          color: Colors.black.withOpacity(0.1),
                          blurRadius: 8,
                          offset: const Offset(0, 2),
                        ),
                      ],
                    ),
                    child: Row(
                      children: [
                        IconButton(
                          icon: const Icon(Icons.arrow_back, color: Colors.white),
                          onPressed: () => Navigator.pop(context),
                        ),
                        Expanded(
                          child: Column(
                            crossAxisAlignment: CrossAxisAlignment.start,
                            children: [
                              Text(
                                widget.job?.title??widget.title,
                                style: Theme.of(context).textTheme.titleMedium?.copyWith(
                                  fontWeight: FontWeight.bold,
                                  color: Colors.white,
                                  fontSize: 18,
                                ),
                                maxLines: 1,
                                overflow: TextOverflow.ellipsis,
                              ),
                              if (widget.job?.company!=null||widget.company!= null)
                                Text(
                                  widget.job?.company??widget.company!,
                                  style: Theme.of(context).textTheme.bodySmall?.copyWith(
                                    color: Colors.white.withOpacity(0.9),
                                    fontSize: 14,
                                  ),
                                  maxLines: 1,
                                  overflow: TextOverflow.ellipsis,
                                ),
                            ],
                          ),
                        ),
                        const SizedBox(width: 12),
                        ElevatedButton(
                          onPressed: () {
                            _launchInNativeBrowser(url);
                          },
                          style: ElevatedButton.styleFrom(
                            padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 8),
                            backgroundColor: Colors.white,
                            foregroundColor: const Color(0xFFFFA726),
                            shape: RoundedRectangleBorder(
                              borderRadius: BorderRadius.circular(8),
                            ),
                            elevation: 0,
                            textStyle: const TextStyle(
                              fontSize: 14,
                              fontWeight: FontWeight.w600,
                            ),
                          ).copyWith(
                            overlayColor: MaterialStateProperty.all(
                              const Color(0xFFFFA726).withOpacity(0.1),
                            ),
                          ),
                          child: const Text('Open in Browser'),
                        ),
                      ],
                    ),
                  ),
                  // Progress indicator
                  if (_progress > 0 && _progress < 1)
                    LinearProgressIndicator(
                      value: _progress,
                      backgroundColor: Colors.white.withOpacity(0.3),
                      valueColor: const AlwaysStoppedAnimation<Color>(Color(0xFFFFA726)),
                    ),
                  // WebView content
                  Expanded(
                    child: Stack(
                      children: [
                        InAppWebView(
                          initialUrlRequest: URLRequest(url: WebUri(url)),
                          initialSettings: InAppWebViewSettings(
                            useShouldOverrideUrlLoading: true,
                            mediaPlaybackRequiresUserGesture: false,
                            javaScriptEnabled: true,
                            javaScriptCanOpenWindowsAutomatically: true,
                          ),
                          onWebViewCreated: (controller) {
                            _webViewController = controller;
                          },
                          onLoadStart: (controller, url) {
                            setState(() {
                              _isLoading = true;
                            });
                          },
                          onLoadStop: (controller, url) {
                            setState(() {
                              _isLoading = false;
                            });
                          },
                          onProgressChanged: (controller, progress) {
                            setState(() {
                              _progress = progress / 100;
                            });
                          },
                          onLoadError: (controller, url, code, message) {
                            setState(() {
                              _isLoading = false;
                            });
                            ScaffoldMessenger.of(context).showSnackBar(
                              SnackBar(content: Text('Error loading page: $message')),
                            );
                          },
                        ),
                        if (_isLoading)
                          Center(
                            child: ModernLoader(
                              size: 60.0,
                              gradientColors: [
                                const Color(0xFFFFA726),
                                const Color(0xFFFF8C00),
                                const Color(0xFFFFA726).withOpacity(0.4),
                              ],
                            ),
                          ),
                      ],
                    ),
                  ),
                ],
              ),
            ),
          );
        }
      ),
    );
  }

  void _launchInNativeBrowser(String url) async {
    final Uri uri = Uri.parse(url);
    if (await canLaunchUrl(uri)) {
      await launchUrl(uri, mode: LaunchMode.externalApplication);
    } else {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text('Could not launch $url')),
      );
    }
  }
}