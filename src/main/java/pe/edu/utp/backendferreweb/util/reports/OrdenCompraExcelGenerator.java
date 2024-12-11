package pe.edu.utp.backendferreweb.util.reports;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import pe.edu.utp.backendferreweb.presentation.dto.response.AlmacenResponse;
import pe.edu.utp.backendferreweb.presentation.dto.response.DetalleCompraResponse;
import pe.edu.utp.backendferreweb.presentation.dto.response.OrdenCompraResponse;
import pe.edu.utp.backendferreweb.presentation.dto.response.ProveedorResponse;

import java.io.*;
import java.time.LocalDate;
import java.util.List;

@Component
public class OrdenCompraExcelGenerator {
    @Value("${templates.xlsx.paths.orden-compra}")
    private String pathTemplateOrden;

    private final int NUM_FILAS = 10;
    private final int FILA_INICIAL = 17;
    private final int FILA_FINAL = FILA_INICIAL + NUM_FILAS;

    public File generateOrdenCompra(OrdenCompraResponse ordenCompra) throws IOException {
        File tempFile = File.createTempFile("temp_excel", ".xlsx");

        try (InputStream inputStream = OrdenCompraExcelGenerator.class.getClassLoader().getResourceAsStream(pathTemplateOrden);
             FileOutputStream outputStream = new FileOutputStream(tempFile)) {
            if (inputStream == null) {
                throw new FileNotFoundException("La plantilla de Excel no fue encontrada en la ruta");
            }

            crearArchivoTemporal(inputStream, outputStream);
            generarExcel(tempFile, ordenCompra);

        }
        return tempFile;
    }

    private void generarExcel(File tempFile, OrdenCompraResponse ordenCompra) throws IOException {
        try (FileInputStream fis = new FileInputStream(tempFile);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);

            fillAllData(ordenCompra, sheet);

            try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                workbook.write(fos);
            }
        }
    }

    private void crearArchivoTemporal(InputStream inputStream, FileOutputStream outputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
    }

    private void fillAllData(OrdenCompraResponse ordenCompra, Sheet sheet) {
        fillStaticData(ordenCompra, sheet);
        fillDinamicData(ordenCompra.getDetalles(), sheet);
    }

    private void fillDinamicData(List<DetalleCompraResponse> detalles, Sheet sheet) {
        final int NUM_PRODUCTOS = detalles.size();

        if (NUM_PRODUCTOS > NUM_FILAS) {
            sheet.shiftRows(FILA_FINAL, sheet.getLastRowNum(), NUM_PRODUCTOS - NUM_FILAS);
        }

        fillProductosData(detalles, sheet);

        if (NUM_PRODUCTOS > NUM_FILAS) {
            establecerFormatoProductos(sheet, NUM_PRODUCTOS - NUM_FILAS);
        }
    }

    private void fillProductosData(List<DetalleCompraResponse> detalles, Sheet sheet) {
        int rowIndex = FILA_INICIAL;
        int numeroProducto = 1;
        for (DetalleCompraResponse detalle : detalles) {
            fillProductoData(rowIndex, numeroProducto, detalle, sheet);
            rowIndex++;
            numeroProducto++;
        }
    }

    private void fillProductoData(int iRow, int numeroProducto, DetalleCompraResponse detalle, Sheet sheet) {
        setCellValue(sheet, iRow, 0, numeroProducto);
        setCellValue(sheet, iRow, 1, detalle.getProducto());
        setCellValue(sheet, iRow, 3, detalle.getUnidad());
        setCellValue(sheet, iRow, 4, detalle.getCantidad());
        setCellValue(sheet, iRow, 5, detalle.getPrecioUnitario());
        setCellValue(sheet, iRow, 6, detalle.getSubtotal());
    }

    private void fillStaticData(OrdenCompraResponse ordenCompra, Sheet sheet) {
        fillGeneralData(ordenCompra, sheet);
        fillFechasData(ordenCompra, sheet);
        fillProveedorData(ordenCompra.getProveedor(), sheet);
        fillAlmacenData(ordenCompra.getDestino(), sheet);
        fillResultsData(ordenCompra, sheet);
    }

    private void fillGeneralData(OrdenCompraResponse ordenCompra, Sheet sheet) {
        String numeroOrden = String.format("OC%08d", ordenCompra.getIdOrdenCompra());
        String metodoEntrega = ordenCompra.getTipoEntrega().getTipo();
        String metodoPago = ordenCompra.getMetodoPago().getNombre();
        String solicitante = ordenCompra.getUsuarioSolicitante().getNombreCompleto();
        String autorizacion = "";

        if (ordenCompra.getUsuarioAutorizacion() != null) {
            autorizacion = ordenCompra.getUsuarioAutorizacion().getNombreCompleto();
        }

        setCellValue(sheet, 3, 6, numeroOrden);
        setCellValue(sheet, 14, 0, metodoEntrega);
        setCellValue(sheet, 14, 2, metodoPago);
        setCellValue(sheet, 14, 4, solicitante);
        setCellValue(sheet, 35, 2, autorizacion);
    }

    private void fillFechasData(OrdenCompraResponse ordenCompra, Sheet sheet) {
        String fechaEmision = ordenCompra.getFechaEmision();
        String fechaEsperada = ordenCompra.getFechaEsperada();

        setCellValue(sheet, 1, 6, fechaEmision);
        setCellValue(sheet, 2, 6, fechaEsperada);
    }

    private void fillProveedorData(ProveedorResponse proveedor, Sheet sheet) {
        String nombreProveedor = proveedor.getNombre();
        String direccionProveedor = proveedor.getDireccion();
        String telefonoProveedor = proveedor.getTelefono();

        setCellValue(sheet, 9, 0, nombreProveedor);
        setCellValue(sheet, 10, 0, direccionProveedor);
        setCellValue(sheet, 11, 0, telefonoProveedor);
    }

    private void fillAlmacenData(AlmacenResponse almacen, Sheet sheet) {
        String nombreAlmacen = almacen.getNombre();
        String direccionAlmacen = almacen.getDireccion();

        setCellValue(sheet, 9, 4, nombreAlmacen);
        setCellValue(sheet, 10, 4, direccionAlmacen);
    }

    private void fillResultsData(OrdenCompraResponse ordenCompra, Sheet sheet) {
        Double subtotal = ordenCompra.getSubtotal();
        Double envio = ordenCompra.getPrecioEnvio();
        Double igv = ordenCompra.getIgv();
        Double otrosPagos = ordenCompra.getOtrosPagos();
        Double total = ordenCompra.getTotal();

        setCellValue(sheet, 28, 6, subtotal);
        setCellValue(sheet, 29, 6, envio);
        setCellValue(sheet, 30, 6, igv);
        setCellValue(sheet, 31, 6, otrosPagos);
        setCellValue(sheet, 32, 6, total);
    }

    private void setCellValue(Sheet sheet, int iRow, int iCol, Object value) {
        Row row = sheet.getRow(iRow);

        if (row == null) {
            row = sheet.createRow(iRow);
        }

        Cell cell = row.getCell(iCol);

        if (cell == null) {
            cell = row.createCell(iCol);
        }

        if (value instanceof String str) {
            cell.setCellValue(str);
        } else if (value instanceof Double dec) {
            cell.setCellValue(dec);
        } else if (value instanceof Integer ent) {
            cell.setCellValue(ent);
        } else if (value instanceof LocalDate date) {
            cell.setCellValue(date);
        }

        cell.setCellValue(value.toString());
    }

    private void establecerFormatoProductos(Sheet sheet, final int FILAS_SOBRANTES) {
        Row filaFormato = sheet.getRow(FILA_INICIAL);
        if (filaFormato != null) {
            for (int i = FILA_FINAL; i < FILA_FINAL + FILAS_SOBRANTES; i++) {
                Row filaActual = sheet.getRow(i);
                copiarFormatoFila(filaFormato, filaActual);
            }
        }
    }

    private void copiarFormatoFila(Row filaOrigen, Row fileObjetivo) {
        for (int i = 0; i < filaOrigen.getLastCellNum(); i++) {
            Cell sourceCell = filaOrigen.getCell(i);
            if (sourceCell != null) {
                Cell targetCell = fileObjetivo.getCell(i);
                if (targetCell == null) {
                    targetCell = fileObjetivo.createCell(i);
                }

                CellStyle newCellStyle = fileObjetivo.getSheet().getWorkbook().createCellStyle();
                newCellStyle.cloneStyleFrom(sourceCell.getCellStyle());
                targetCell.setCellStyle(newCellStyle);
            }
        }
    }
}
