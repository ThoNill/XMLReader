package janus.reader.test;

import static org.junit.Assert.fail;
import janus.reader.Formater;
import janus.reader.Reader;
import janus.reader.TagReader;
import janus.reader.adapters.BooleanAdapter;
import janus.reader.adapters.DoubleAdapter;
import janus.reader.adapters.FloatAdapter;
import janus.reader.adapters.IntegerAdapter;
import janus.reader.adapters.LongAdapter;
import janus.reader.attribute.Attribute;
import janus.reader.core.ValuesAndAttributesContainer;
import janus.reader.exceptions.ReaderRuntimeException;
import janus.reader.helper.ClassHelper;
import janus.reader.nls.Messages;
import janus.reader.path.XmlElementPath;
import janus.reader.test.entities.Child;
import janus.reader.test.entities.City;
import janus.reader.test.entities.LinkChild;
import janus.reader.test.entities.TAnnotated;
import janus.reader.test.entities.TObject;
import janus.reader.test.entities.TStaticAnnotated;
import janus.reader.test.entities.TWrongAnnotated;
import janus.reader.test.entities.TWrongFunctionNameAnnotated;
import janus.reader.test.entities.TWrongFunctionParameterCountAnnotated;
import janus.reader.test.entities.TWrongStaticFunctionParameterCountAnnotated;
import janus.reader.test.entities.TWrongStaticFunctionReturnWrongTypeAnnotated;
import janus.reader.value.CurrentObject;
import janus.reader.value.SimpleCurrentObject;
import janus.reader.value.StackCurrentObject;
import janus.reader.value.Value;
import janus.reader.value.ValueMap;

import java.io.IOException;
import java.text.ParseException;
import java.util.NoSuchElementException;

import javax.xml.stream.XMLStreamException;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationTest {
    private static final Logger log = LoggerFactory.getLogger(Value.class);

    private static final String KONTOAUSZUG_XML = "src/test/resources/kontoauszug.xml";
    private static final String KONTOAUSZUG_XML2 = "src/test/resources/kontoauszug2.xml";
    private static final String CHILDS_XML = "src/test/resources/childs.xml";
    private static final String WRONG_XML = "src/test/resources/wrongChilds.xml";

    private static final String CHILDSANDCITY_XML = "src/test/resources/childsAndCity.xml";

    @Test
    public void wrongAnnotatedClass() {
        try {
            new Reader(TObject.class);
            fail("Keine Ausnahme");
        } catch (Exception ex) {
            log.info("Expected exception", ex);
        }
    }

    @Test
    public void wrongAnnotatedMethod() {
        try {
            new Reader(TWrongAnnotated.class);
            fail("Keine Ausnahme");
        } catch (Exception ex) {
            log.info("Expected exception", ex);
        }
    }

    @Test
    public void wrongFunctionName() {
        try {
            new Reader(TWrongFunctionNameAnnotated.class);
            fail("Keine Ausnahme");
        } catch (Exception ex) {
            log.info("Expected exception", ex);
        }
    }

    @Test
    public void wrongFunctionParameterCountName() {
        try {
            new Reader(TWrongFunctionParameterCountAnnotated.class);
            fail("Keine Ausnahme");
        } catch (Exception ex) {
            log.info("Expected exception", ex);
        }
    }

    @Test
    public void wrongStaticFunctionParameterCount() {
        try {
            new Reader(TWrongStaticFunctionParameterCountAnnotated.class);
            fail("Keine Ausnahme");
        } catch (Exception ex) {
            log.info("Expected exception", ex);
        }
    }

    @Test
    public void wrongStaticFunctionReturnType() {
        try {
            new Reader(TWrongStaticFunctionReturnWrongTypeAnnotated.class);
            fail("Keine Ausnahme");
        } catch (Exception ex) {
            log.info("Expected exception", ex);
        }
    }


    @Test
    public void readAnnotatedClass() {
        try {
            Reader reader = new Reader(TAnnotated.class);
        } catch (Exception e) {
            log.error("unerwartete Ausnahme", e);
            fail("Nicht erwartete Ausnahme");
        }
    }

    @Test
    public void readAnnotatedClass2() {
        try {
            Reader reader = new Reader(TAnnotated.class);
        } catch (Exception e) {
            log.error("unerwartete Ausnahme", e);
            fail("Nicht erwartete Ausnahme");
        }

    }

    @Test
    public void readStaticAnnotatedClass() {
        try {
            Reader reader = new Reader(TStaticAnnotated.class);
        } catch (Exception e) {
            log.error("unerwartete Ausnahme", e);
            fail("Nicht erwartete Ausnahme");
        }

    }

}
