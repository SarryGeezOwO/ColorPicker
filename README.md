# ColorPicker
A Java Swing component for picking colors with a very
nice-looking GUI. This component uses [MigLayout](https://github.com/mikaelgrev/miglayout) to position components.


> [!NOTE]
> Work in progress, current ass of an implementation
> but it works very well for now. All it needs is a few refactors
> here and there.
>
> Also, this package is severely lacking in documentation for now so, sorry for that 😔😔

> [!CAUTION]
> Be aware not everything is handled with exceptions yet, so there are some cases like
> Inputting a number above 300 on one of the RGB text fields will cause an error

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

### Features wanted
- Documentation and JavaDocs, even the most simplest, will be nice
- Eye dropper
- Color history

### Licence
This repo uses M.I.T licensing, so feel free to go wild
with this JComponent, though,  crediting will be appreciated :) 
