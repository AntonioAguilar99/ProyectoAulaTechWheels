package techwheels.Clases;






public class CarritoTemp  {
    private Long id;
    private String nombreProducto;
    private String descripcionProducto;
    private String marcaProducto;
    private String categoriaProducto;
    private double precioProducto;
    private int cantidad;
      
   
    

    public CarritoTemp() {
    }

    public CarritoTemp(String nombreProducto, String descripcionProducto, String marcaProducto,String categoriaProducto, double precioProducto, int cantidad) {
       
    
        this.nombreProducto = nombreProducto;
        this.descripcionProducto = descripcionProducto;
        this.marcaProducto = marcaProducto;
        this.categoriaProducto = categoriaProducto;
        this.precioProducto = precioProducto;
        this.cantidad = cantidad;
      
       
    }

    // Getters y Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public String getDescripcionProducto() {
        return descripcionProducto;
    }

    public void setDescripcionProducto(String descripcionProducto) {
        this.descripcionProducto = descripcionProducto;
    }
    
     public String getMarcaProducto() {
        return marcaProducto;
    }
     
    public void setMarcaProducto(String marcaProducto) {
        this.marcaProducto = marcaProducto;
    }
    
     public String getCategoriaProducto() {
        return categoriaProducto;
    }
     
    public void setCategoriaProducto(String categoriaProducto) {
        this.categoriaProducto = categoriaProducto;
    }

    public double getPrecioProducto() {
        return precioProducto;
    }

    public void setPrecioProducto(double precioProducto) {
        this.precioProducto = precioProducto;
      
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
      
    }

   

   
}


