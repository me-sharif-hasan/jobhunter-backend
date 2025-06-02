import 'package:flutter/cupertino.dart';
import 'package:personalized_job_hunter/features/common/controller/meta_controller.dart';
import 'package:personalized_job_hunter/util/values/widget_loading_registry.dart';
import 'package:provider/provider.dart';

class ComponentLoader extends StatelessWidget {
  final WidgetLoadingRegistry name;
  final Widget child;

  const ComponentLoader({
    Key? key,
    required this.name,
    required this.child,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Consumer<MetaController>(
      builder: (context, controller, _) {
        return controller.getComponentLoading(name)?
            const Center(child: CupertinoActivityIndicator()):
            child;
      },
    );
  }
}