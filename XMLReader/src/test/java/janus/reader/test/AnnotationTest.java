package janus.reader.test;

import static org.junit.Assert.fail;
import janus.reader.Reader;
import janus.reader.test.entities.TAnnotated;
import janus.reader.test.entities.TObject;
import janus.reader.test.entities.TStaticAnnotated;
import janus.reader.test.entities.TWrongAnnotated;
import janus.reader.test.entities.TWrongFunctionNameAnnotated;
import janus.reader.test.entities.TWrongFunctionParameterCountAnnotated;
import janus.reader.test.entities.TWrongStaticFunctionParameterCountAnnotated;
import janus.reader.test.entities.TWrongStaticFunctionReturnWrongTypeAnnotated;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationTest {
    private static final Logger log = LoggerFactory.getLogger(AnnotationTest.class);
    private static final String EXCEPTION_NOT_EXPECTED = "this exception is a error";
    private static final String EXCEPTION_EXPECTED = "Exception expected";
 

    @Test
    public void wrongAnnotatedClass() {
        try {
            new Reader(TObject.class);
            fail(EXCEPTION_EXPECTED);
        } catch (Exception ex) {
            log.info(EXCEPTION_EXPECTED, ex);
        }
    }

    @Test
    public void wrongAnnotatedMethod() {
        try {
            new Reader(TWrongAnnotated.class);
            fail(EXCEPTION_EXPECTED);
        } catch (Exception ex) {
            log.info(EXCEPTION_EXPECTED, ex);
        }
    }

    @Test
    public void wrongFunctionName() {
        try {
            new Reader(TWrongFunctionNameAnnotated.class);
            fail(EXCEPTION_EXPECTED);
        } catch (Exception ex) {
            log.info(EXCEPTION_EXPECTED, ex);
        }
    }

    @Test
    public void wrongFunctionParameterCountName() {
        try {
            new Reader(TWrongFunctionParameterCountAnnotated.class);
            fail(EXCEPTION_EXPECTED);
        } catch (Exception ex) {
            log.info(EXCEPTION_EXPECTED, ex);
        }
    }

    @Test
    public void wrongStaticFunctionParameterCount() {
        try {
            new Reader(TWrongStaticFunctionParameterCountAnnotated.class);
            fail(EXCEPTION_EXPECTED);
        } catch (Exception ex) {
            log.info(EXCEPTION_EXPECTED, ex);
        }
    }

    @Test
    public void wrongStaticFunctionReturnType() {
        try {
            new Reader(TWrongStaticFunctionReturnWrongTypeAnnotated.class);
            fail(EXCEPTION_EXPECTED);
        } catch (Exception ex) {
            log.info(EXCEPTION_EXPECTED, ex);
        }
    }


    @Test
    public void readAnnotatedClass() {
        try {
            Reader reader = new Reader(TAnnotated.class);
        } catch (Exception e) {
            log.error(EXCEPTION_NOT_EXPECTED, e);
            fail(EXCEPTION_NOT_EXPECTED);
        }
    }

    @Test
    public void readAnnotatedClass2() {
        try {
            Reader reader = new Reader(TAnnotated.class);
        } catch (Exception e) {
            log.error(EXCEPTION_NOT_EXPECTED, e);
            fail(EXCEPTION_NOT_EXPECTED);
        }

    }

    @Test
    public void readStaticAnnotatedClass() {
        try {
            Reader reader = new Reader(TStaticAnnotated.class);
        } catch (Exception e) {
            log.error(EXCEPTION_NOT_EXPECTED, e);
            fail(EXCEPTION_NOT_EXPECTED);
        }

    }

}
