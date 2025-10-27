package org.jramirezdfernandez.monolitonaruto.exportacion;

public interface ElementToVisit {
    byte[] aceptarExportarFormato(VisitorFormato formato);

    String getName();
}
