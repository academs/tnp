<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:param name="contextPath" />
    <xsl:param name="selectForFilm" />

    <xsl:template match="/">
        <xsl:choose>
            <xsl:when test="xmlMessage/directors/director">
                <table  class="table table-striped">
                    <thead>
                        <tr>
                            <td>#</td>
                            <td>Name</td>
                            <td>Phone</td>
                            <td />
                        </tr>
                    </thead>
                    <xsl:for-each select="xmlMessage/directors/director">
                        <tr>
                            <td>
                                <xsl:value-of select="idDirector"/>
                            </td>
                            <td>
                                <a href="{$contextPath}/directors/edit?id={idDirector}">
                                    <xsl:value-of select="name"/>
                                </a>
                            </td>
                            <td>
                                <xsl:value-of select="phone"/>
                            </td>
                            <td>
                                <form action="{$contextPath}/directors/delete" method="POST">
                                    <input type="hidden" name="id" value="{idDirector}" />
                                    <button type="submit" class="btn btn-info">
                                        <span class="glyphicon glyphicon-trash" />Удалить
                                    </button>
                                </form>
                            </td>
                        </tr>
                    </xsl:for-each>
                </table>
            </xsl:when>
            <xsl:otherwise>
                <br/>
                <div class="alert alert-info">
                    No records
                </div>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
</xsl:stylesheet>