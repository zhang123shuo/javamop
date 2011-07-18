//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-793 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.12.05 at 03:18:32 PM CST 
//


package logicrepository.parser.logicrepositorysyntax;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the LogicRepository.parser.LogicRepositorySyntax package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Mop_QNAME = new QName("", "mop");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: LogicRepository.parser.LogicRepositorySyntax
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link PropertyType }
     * 
     */
    public PropertyType createPropertyType() {
        return new PropertyType();
    }

    /**
     * Create an instance of {@link StatisticsType }
     * 
     */
    public StatisticsType createStatisticsType() {
        return new StatisticsType();
    }

    /**
     * Create an instance of {@link LogicRepositoryType }
     * 
     */
    public LogicRepositoryType createLogicRepositoryType() {
        return new LogicRepositoryType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LogicRepositoryType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "mop")
    public JAXBElement<LogicRepositoryType> createMop(LogicRepositoryType value) {
        return new JAXBElement<LogicRepositoryType>(_Mop_QNAME, LogicRepositoryType.class, null, value);
    }

}
