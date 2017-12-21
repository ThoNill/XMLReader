package janus.reader.path;

import janus.reader.util.Assert;

import javax.xml.namespace.QName;

/**
 * The Path of the Tags
 * 
 * @author Thomas Nill
 *
 */
public class XmlElementPath {
    private static final String THE_PATH_SHOULD_NOT_BE_NULL = "The path should not be null";
    private String path;
    private QName[] parts;

    /**
     * Constructor
     * 
     * @param path
     */
    public XmlElementPath(String path) {
        super();
        Assert.notNull(path, THE_PATH_SHOULD_NOT_BE_NULL);

        this.path = path;
        this.parts = createQNames(splitInStrings(path));
    }

    /**
     * Constructor
     * 
     * @param parts
     */
    public XmlElementPath(QName[] parts) {
        super();
        Assert.notNull(parts, THE_PATH_SHOULD_NOT_BE_NULL);

        this.path = buildThePathString(parts);
        this.parts = parts;
    }

    /**
     * Constructor
     * 
     * @param path
     */
    public XmlElementPath(XmlElementPath path) {
        this.path = path.path;
        this.parts = path.parts;
    }

    /**
     * compares two paths
     * 
     * @param pathFromAnnotation
     * @return
     */
    public boolean compare(XmlElementPath pathFromAnnotation) {
        Assert.notNull(pathFromAnnotation, THE_PATH_SHOULD_NOT_BE_NULL);

        if (pathFromAnnotation.isAbsolut()) {
            boolean ok = this.parts.length == pathFromAnnotation.parts.length;
            if (ok) {
                ok = endsWith(pathFromAnnotation);
            }
            return ok;

        }
        return endsWith(pathFromAnnotation);
    }

    /**
     * starts with this XmlElementPath
     * 
     * @param path
     * @return
     */
    public boolean startsWith(XmlElementPath path) {
        Assert.notNull(path, THE_PATH_SHOULD_NOT_BE_NULL);

        for (int okIndex = 0; okIndex < path.parts.length; okIndex++) {
            if (okIndex >= parts.length
                    || (!asymmetricCompare(path.parts[okIndex], parts[okIndex]))) {
                return false;
            }
        }
        return true;
    }

    /**
     * ends with this XmlElementPath
     * 
     * @param pathFromAnnotation
     * @return
     */
    public boolean endsWith(XmlElementPath pathFromAnnotation) {
        Assert.notNull(pathFromAnnotation, THE_PATH_SHOULD_NOT_BE_NULL);

        int index = parts.length - 1;
        for (int okIndex = pathFromAnnotation.parts.length - 1; okIndex >= 0; okIndex--, index--) {
            if (index < 0
                    || (!asymmetricCompare(pathFromAnnotation.parts[okIndex],
                            parts[index]))) {
                return false;
            }
        }
        // this.path ends with a absolute path.path only, when the lengths are
        // equal
        // eg: /c is not an end of /a/b/c but c is an end of /a/b/c
        // eg: /b/c is not an end of /a/b/c but b/c is an end of /a/b/c
        return this.parts.length == pathFromAnnotation.parts.length
                || !pathFromAnnotation.isAbsolut();

    }

    /**
     * get the paranet path example: the paranet path of /a/b/c is /a/b
     * 
     * @return
     */
    public XmlElementPath parent() {
        Assert.notNull(path, THE_PATH_SHOULD_NOT_BE_NULL);

        int lastIndex = this.path.lastIndexOf('/');
        if (lastIndex >= 0) {
            return new XmlElementPath(this.path.substring(0, lastIndex));
        }
        return null;
    }

    /**
     * concatenation of paths
     * 
     * @param path
     * @return
     */
    public XmlElementPath concat(XmlElementPath path) {
        Assert.notNull(path, THE_PATH_SHOULD_NOT_BE_NULL);

        return new XmlElementPath(this.path + "/" + path.path);
    }

    /**
     * is a absolute path example: /a/b/c
     * 
     * @return
     */
    public boolean isAbsolut() {
        return path.charAt(0) == '/';
    }

    public int getDepth() {
        return parts.length + 1;
    }

    public String getPath() {
        return path;
    }

    private String[] splitInStrings(String path) {
        String splitPath = ignoreFirstSlash(path);
        return splitPath.split("\\/");
    }

    private String ignoreFirstSlash(String path) {
        String splitPath = path;
        if (path.length() > 0 && path.charAt(0) == '/') {
            splitPath = path.substring(1);
        }
        return splitPath;
    }

    private QName[] createQNames(String[] sparts) {
        QName[] qNames = new QName[sparts.length];
        for (int i = 0; i < sparts.length; i++) {
            qNames[i] = createQName(sparts[i]);
        }
        return qNames;
    }

    /**
     * Create a QName from a String of the form
     * {namespace...}prefix:localName...
     * 
     * @param text
     * @return
     */
    private QName createQName(String text) {
        if (text.length() == 0) {
            return new QName("");
        }
        checkBeforeQNameSplitting(text);

        int posColon = text.indexOf(':');
        int posBraceEnd = text.indexOf('}');

        String namespace = extractNamespace(text, posColon, posBraceEnd);

        String textWithoutNamespace = extractText(text, posBraceEnd);
        posColon = textWithoutNamespace.indexOf(':');
        String prefix = extractPrefix(textWithoutNamespace, posColon);
        String localPart = extractText(textWithoutNamespace, posColon);

        if (namespace == null) {
            return new QName(localPart);
        }
        return new QName(namespace, localPart, prefix);
    }

    private void checkBeforeQNameSplitting(String text) {
        int posBraceStart = text.indexOf('{');
        int posBraceEnd = text.indexOf('}');
        int posColon = text.indexOf(':');
        int length = text.length();

        boolean colonBeforeBraceEnd = posColon >= 0 && posColon < posBraceEnd;
        // this includes non existing braces
        boolean braceEndBeforeBraceStart = posBraceEnd < posBraceStart;
        boolean colonAtStartOrEnd = posColon == 0 || posColon == length - 1;
        boolean braceStartNotAtBegin = posBraceStart > 0;

        if (braceStartNotAtBegin || colonBeforeBraceEnd
                || braceEndBeforeBraceStart || colonAtStartOrEnd) {
            throw new IllegalArgumentException("The text " + text
                    + " is not of the form {namespace...}prefix:localName... ");
        }
    }

    private String extractNamespace(String text, int posColon, int posBraceEnd) {
        String namespace = extractPrefix(text, posBraceEnd);
        if (namespace != null) {
            namespace = namespace.substring(1);
        }
        return namespace == null && posColon >= 0 ? "" : namespace;
    }


    private String extractText(String text, int startPosition) {
        return text.substring(startPosition < 0 ? 0 : startPosition + 1);
    }

    private String extractPrefix(String text, int endPosition) {
        return endPosition < 0 ? null : text.substring(0, endPosition);
    }

    private String buildThePathString(QName[] parts) {
        StringBuilder builder = new StringBuilder();
        for (QName qn : parts) {
            builder.append("/");
            String namespace = qn.getNamespaceURI();
            builder.append(namespace == null || "".equals(namespace.trim()) ? ""
                    : "{" + namespace + "}");
            String prefix = qn.getPrefix();
            builder.append(prefix == null || "".equals(prefix.trim()) ? ""
                    : prefix + ":");
            builder.append(qn.getLocalPart());
        }

        return builder.toString();
    }


    /**
     * this comparation is a little asymmetric, because perhapsWithoutNamespace
     * came from the class annotations and withNameSpace came from the
     * StaxReader in the XML.
     * 
     * If the annotation has no namespace, it should be equal to a XML element
     * with the default namespace of the document. If the annotation has a
     * prefix, it should not be equal to a any XML element with another prefix
     * or no prefix.
     * 
     * @param perhapsWithoutNamespace
     * @param withNameSpace
     * @return
     */
    private static boolean asymmetricCompare(QName perhapsWithoutNamespace,
            QName withNameSpace) {
        String nsW = withNameSpace.getNamespaceURI();
        String nsP = perhapsWithoutNamespace.getNamespaceURI();

        String prefixW = withNameSpace.getPrefix();
        String prefixP = perhapsWithoutNamespace.getPrefix();

        // QNames.getLocalPart() can not be null
        boolean equalLocalParts = withNameSpace.getLocalPart().equals(
                perhapsWithoutNamespace.getLocalPart());

        boolean nearlyEqualNamespaces = nsP == null || "".equals(nsP)
                || nsP.equals(nsW);

        boolean emptyPrefixP = prefixP == null || "".equals(prefixP);
        boolean emptyPrefixW = prefixW == null || "".equals(prefixW);
        boolean emptyPrefixes = emptyPrefixP && emptyPrefixW;

        boolean equalExistingPrefixes = prefixP != null
                && prefixP.equals(prefixW);
        boolean equalPrefixes = emptyPrefixes || equalExistingPrefixes;

        return equalLocalParts && nearlyEqualNamespaces && equalPrefixes;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        return prime * result + ((path == null) ? 0 : path.hashCode());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        XmlElementPath other = (XmlElementPath) obj;
        if (path == null) {
            if (other.path != null)
                return false;
        } else if (!path.equals(other.path))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return path;
    }

}
