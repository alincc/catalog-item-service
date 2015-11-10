package no.nb.microservices.catalogitem.rest.controller.assembler;

import no.nb.microservices.catalogitem.rest.model.RecordInfo;
import no.nb.microservices.catalogmetadata.model.mods.v3.Mods;
import no.nb.microservices.catalogmetadata.model.mods.v3.RecordIdentifier;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 * Created by raymondk on 9/14/15.
 */
public class RecordInfoBuilderTest {

    @Test
    public void whenRecordInfoIsvalid() {
        no.nb.microservices.catalogmetadata.model.mods.v3.RecordInfo recordInfo = new no.nb.microservices.catalogmetadata.model.mods.v3.RecordInfo();
        recordInfo.setRecordIdentifier(getRecordIdentifier());
        recordInfo.setCreationDate(new Date());

        Mods mods = new Mods();
        mods.setRecordInfo(recordInfo);

        RecordInfoBuilder builder = new RecordInfoBuilder().withRecordInfo(mods.getRecordInfo());
        RecordInfo build = builder.build();

        assertEquals("111321352", build.getIdentifier());
        assertEquals("NO-TrBIB", build.getIdentifierSource());
        assertEquals(new Date().toString(), build.getCreated());
    }

    private RecordIdentifier getRecordIdentifier() {
        RecordIdentifier recordIdentifier = new RecordIdentifier();
        recordIdentifier.setSource("NO-TrBIB");
        recordIdentifier.setValue("111321352");
        return recordIdentifier;
    }

}