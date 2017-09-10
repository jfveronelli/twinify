package ar.org.crossknight.twinify.serialization;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import ar.org.crossknight.twinify.domain.delta.ExtensionFilter;
import ar.org.crossknight.twinify.domain.delta.Filter;
import ar.org.crossknight.twinify.domain.delta.PathFilter;

public final class FilterSerializer {

    private static final String EOL = "\n";
    private static final String COMMENT_TAG = "#";

    private FilterSerializer() {}

    public static final void write(OutputStream stream, List<Filter> filters) throws IOException {
        Writer writer = new OutputStreamWriter(new BufferedOutputStream(stream), "UTF-8");
        for (Filter filter: filters) {
            writer.write(filter + EOL);
        }
        writer.close();
    }

    public static final List<Filter> read(InputStream stream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new BufferedInputStream(stream), "UTF-8"));
        List<Filter> filters = new ArrayList<Filter>();

        String line;
        while ((line = reader.readLine()) != null) {
            if (line.length() == 0 || line.startsWith(COMMENT_TAG)) {
                continue;
            }
            boolean includes = line.startsWith(Filter.INCLUDE_TAG);
            line = line.substring(1);
            if (line.startsWith(PathFilter.HEADER)) {
                String path = line.substring(PathFilter.HEADER.length()).trim();
                filters.add(new PathFilter(includes, path));
            } else if (line.startsWith(ExtensionFilter.HEADER)) {
                String extension = line.substring(ExtensionFilter.HEADER.length()).trim();
                filters.add(new ExtensionFilter(includes, extension));
            } else {
                throw new IOException("Invalid filter definition");
            }
        }

        reader.close();
        return filters;
    }

}