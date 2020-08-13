package com.aspose.psd_layerexporter;

import com.aspose.psd.ImageOptionsBase;
import com.aspose.psd.fileformats.psd.layers.Layer;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImageNameFormatterImpl implements ImageNameFormatter<Layer>
{
    private static final String DEFAULT_FILENAME_TEMPLATE = "{{layer.name}}.{{ext}}";
    private static final String DEFAULT_PLACEHOLDER_PREFIX = "{{";
    private static final String DEFAULT_PLACEHOLDER_SUFFIX = "}}";

    private final String filenameTemplate;
    private final String placeholderPrefix;
    private final String placeholderSuffix;
    private final Pattern placeholderPattern;

    public ImageNameFormatterImpl()
    {
        this(DEFAULT_FILENAME_TEMPLATE);
    }

    public ImageNameFormatterImpl(String filenameTemplate)
    {
        this(filenameTemplate, DEFAULT_PLACEHOLDER_PREFIX, DEFAULT_PLACEHOLDER_SUFFIX);
    }

    protected ImageNameFormatterImpl(String filenameTemplate, String placeholderPrefix, String placeholderSuffix)
    {
        this.filenameTemplate = filenameTemplate;
        this.placeholderPrefix = placeholderPrefix;
        this.placeholderSuffix = placeholderSuffix;
        this.placeholderPattern = compilePlaceholderPattern(placeholderPrefix, placeholderSuffix);
    }

    private static Pattern compilePlaceholderPattern(String prefix, String suffix)
    {
        return Pattern.compile(Pattern.quote(prefix) + "[\\w.]+" + suffix);
    }

    public String format(Layer layer, ImageOptionsBase options)
    {
        String filename = filenameTemplate;
        Map<String, String> placeholderKeyToValueMap = getPlaceholderKeyToValueMap(layer, options);
        for (Map.Entry<String, String> entry : placeholderKeyToValueMap.entrySet())
        {
            Matcher matcher = placeholderPattern.matcher(wrapPlaceholderKey(entry.getKey()));
            while (matcher.find())
            {
                String placeholder = matcher.group();
                filename = filename.replace(placeholder, entry.getValue());
            }
        }

        return filename;
    }

    protected Map<String, String> getPlaceholderKeyToValueMap(Layer layer, ImageOptionsBase options)
    {
        Map<String, String> map = new HashMap<String, String>();
        map.put("layer.name", layer.getName());
        map.put("ext", ImageExtension.getImageExtension(options.getClass()));
        return map;
    }

    private String wrapPlaceholderKey(String placeholderKey)
    {
        return placeholderPrefix + placeholderKey + placeholderSuffix;
    }
}
