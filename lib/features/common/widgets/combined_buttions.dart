import 'package:flutter/material.dart';

class CombinedButtons extends StatelessWidget {
  final List<Widget> buttons;

  const CombinedButtons({super.key, required this.buttons});

  @override
  Widget build(BuildContext context) {
    return ClipRRect(
      borderRadius: BorderRadius.circular(8),
      child: IntrinsicHeight(
        child: Row(
          mainAxisSize: MainAxisSize.min,
          children: List.generate(buttons.length, (index) {
            return Expanded(
              child: _ButtonWrapper(
                isFirst: index == 0,
                isLast: index == buttons.length - 1,
                child: buttons[index],
              ),
            );
          }),
        ),
      ),
    );
  }
}

class _ButtonWrapper extends StatelessWidget {
  final Widget child;
  final bool isFirst;
  final bool isLast;

  const _ButtonWrapper({
    required this.child,
    required this.isFirst,
    required this.isLast,
  });

  @override
  Widget build(BuildContext context) {
    return Container(
      decoration: BoxDecoration(
        // borderRadius: BorderRadius.horizontal(
        //   left: isFirst ? const Radius.circular(8) : Radius.zero,
        //   right: isLast ? const Radius.circular(8) : Radius.zero,
        // ),
      ),
      child: child,
    );
  }
}
