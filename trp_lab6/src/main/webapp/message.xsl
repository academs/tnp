<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:param name="level" />

    <xsl:template match="/">
        <xsl:if test="xmlMessage/message">
            <div class="alert alert-{$level}">
                <xsl:value-of select="xmlMessage/message"/>
            </div>
        </xsl:if>
    </xsl:template>
</xsl:stylesheet>