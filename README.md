# ColorPicker
A java Swing component for picking colors with very
nice looking UI.

> Work in progress, currently ass of an implementation
> but works very well for now, just need some few refactors
> here and there.

### Preview
![img.png](Preview.png)

### Usage
- <code>ColorPicker</code> has a few constructors to configure the 
JPanels radius.
- <code>ColorPicker</code> subclasses JPanel.
- <code>ColorPickerListener</code> has more listener action other than
<code>onColorChanged()</code>
```Java
void demo() {
    ColorPicker picker = new ColorPicker(15);
    picker.setTextFieldBorder(new EmptyBorder(5, 10, 5, 10));
    picker.addColorPickerListener(new ColorPickerListener() {
        @Override
        public void onColorChanged(Color newColor) {
            System.out.println("New Color: " + newColor);
        }
    });
    // Add ColorPicker to a Component
}
```

### Licence
This repo uses M.I.T licencing, so feel free to go wild
with this JComponent, though a bit of credit will be nice :) 
