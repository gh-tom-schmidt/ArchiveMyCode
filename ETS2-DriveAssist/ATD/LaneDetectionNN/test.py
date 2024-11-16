import xml.etree.ElementTree as ET

# Parse the XML document
tree = ET.parse('annotations.xml')
root = tree.getroot()

# Iterate over each <image> tag
for image in root.findall('image'):
    # Iterate over each <polyline> tag inside the <image> tag
    for polyline in image.findall('polyline'):
        # Get the label attribute
        label = polyline.get('label')

        # Modify the label attribute if necessary
        if label == 'vertical':
            # Remove the <polyline> tag completely
            image.remove(polyline)
        else:
            # Change the label to 'solid'
            polyline.set('label', 'solid')

# Write the modified XML document back to a file
tree.write('mod_annotations.xml')
