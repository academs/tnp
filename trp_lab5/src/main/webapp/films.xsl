<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:param name="contextPath" />

    <xsl:template match="/">
        <xsl:choose>
            <xsl:when test="xmlMessage/films/film">
                <table  class="table table-striped">
                    <thead>
                        <tr>
                            <td>#</td>
                            <td>Title</td>
                            <td>Director</td>
                            <td>Genre</td>
                            <td>Dreation</td>
                            <td>Year</td>
                            <td />
                        </tr>
                    </thead>
                    <xsl:for-each select="xmlMessage/films/film">
                        <tr>
                            <td>
                                <xsl:value-of select="idFilm"/>
                            </td>
                            <td>
                                <a href="{$contextPath}/films/edit?id={idFilm}">
                                    <xsl:value-of select="title"/>
                                </a>
                            </td>
                            <td>
                                <xsl:value-of select="directorName"/>
                            </td>
                            <td>
                                <xsl:value-of select="genreName"/>
                            </td>
                            <td>
                                <xsl:value-of select="duration"/>
                            </td>
                            <td>
                                <xsl:value-of select="year"/>
                            </td>
                            <td>
                                <form action="{$contextPath}/films/delete" method="POST">
                                    <input type="hidden" name="id" value="{idFilm}" />
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