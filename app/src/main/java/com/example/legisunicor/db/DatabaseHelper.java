package com.example.legisunicor.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.legisunicor.db.models.Caso;
import com.example.legisunicor.db.models.Reporte;
import com.example.legisunicor.db.models.Roles;
import com.example.legisunicor.db.models.User;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Legis.db";
    private static final int DATABASE_VERSION = 1;

    private int idUsuarioLogueado;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Usuario (" +
                "ID INTEGER PRIMARY KEY," +
                "Username TEXT," +
                "Password TEXT," +
                "Nombre TEXT," +
                "Apellidos TEXT," +
                "idRol INTEGER," +
                "logeado INTEGER DEFAULT 0)");

        db.execSQL("CREATE TABLE Rol (" +
                "IDrol INTEGER PRIMARY KEY," +
                "Rolname TEXT)");

        db.execSQL("CREATE TABLE Casos (" +
                "IDcaso INTEGER PRIMARY KEY," +
                "nombreCaso TEXT," +
                "Descripcionbreve TEXT," +
                "Estado TEXT," +
                "Idcliente INTEGER)");

        db.execSQL("CREATE TABLE AbogadoCaso (" +
                "IDCaso INTEGER," +
                "IDusuario INTEGER," +
                "PRIMARY KEY (IDCaso, IDusuario)," +
                "FOREIGN KEY (IDCaso) REFERENCES Casos(IDcaso)," +
                "FOREIGN KEY (IDusuario) REFERENCES Usuario(ID))");


        db.execSQL("CREATE TABLE Reportes (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "Periodo TEXT," +
                "Descripcion TEXT, " +
                "Enero TEXT," +
                "Febrero TEXT," +
                "Marzo TEXT," +
                "Abril TEXT," +
                "Mayo TEXT," +
                "Junio TEXT," +
                "Julio TEXT," +
                "Agosto TEXT," +
                "Septiembre TEXT," +
                "Octubre TEXT," +
                "Noviembre TEXT," +
                "Diciembre TEXT)"
        );

        db.execSQL("INSERT INTO Rol (IDrol, Rolname) VALUES (1, 'administrador')");
        db.execSQL("INSERT INTO Rol (IDrol, Rolname) VALUES (2, 'abogado')");
        db.execSQL("INSERT INTO Rol (IDrol, Rolname) VALUES (3, 'cliente')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Usuario");
        db.execSQL("DROP TABLE IF EXISTS Rol");
        db.execSQL("DROP TABLE IF EXISTS Casos");
        db.execSQL("DROP TABLE IF EXISTS AbogadoCaso");
        db.execSQL("DROP TABLE IF EXISTS Reportes");

        onCreate(db);
    }

    public User login(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {
                "ID",
                "Username",
                "Password",
                "Nombre",
                "Apellidos",
                "idRol",
                "logeado"
        };

        String selection = "Username = ? AND Password = ?";
        String[] selectionArgs = {username, password};

        Cursor cursor = db.query(
                "Usuario",
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        User usuario = null;
        if (cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("ID"));
            String nombre = cursor.getString(cursor.getColumnIndexOrThrow("Nombre"));
            String apellidos = cursor.getString(cursor.getColumnIndexOrThrow("Apellidos"));
            int idRol = cursor.getInt(cursor.getColumnIndexOrThrow("idRol"));
            int isLogged = cursor.getInt(cursor.getColumnIndexOrThrow("logeado"));

            usuario = new User(id, username, password, nombre, apellidos, idRol, isLogged);
            ContentValues values = new ContentValues();
            values.put("logeado", 1);
            String whereClause = "ID = ?";
            String[] whereArgs = {String.valueOf(id)};

            db.update("Usuario", values, whereClause, whereArgs);

        }

        cursor.close();

        return usuario;
    }

    public boolean registrarUsuario(String username, String password, String nombre, String apellidos, int idRol) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("Username", username);
        values.put("Password", password);
        values.put("Nombre", nombre);
        values.put("Apellidos", apellidos);
        values.put("idRol", idRol);
        values.put("logeado", 0);

        long result = db.insert("Usuario", null, values);

        return result != -1;
    }

    public long crearUsuario(User usuario) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("Username", usuario.getUsername());
        values.put("Password", usuario.getPassword());
        values.put("Nombre", usuario.getNombre());
        values.put("Apellidos", usuario.getApellidos());
        values.put("idRol", usuario.getIdRol());
        values.put("logeado", 0);

        long id = db.insert("Usuario", null, values);


        return id;
    }

    public int eliminarUsuario(int idUsuario) {
        SQLiteDatabase db = this.getWritableDatabase();

        String[] projection = {"IDcaso"};
        String selection = "Idcliente = ?";
        String[] selectionArgs = {String.valueOf(idUsuario)};

        Cursor cursor = db.query(
                "Casos",
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor.getCount() > 0) {
            cursor.close();
            db.close();
            return 0;
        }

        cursor.close();

        String whereClause = "ID = ?";
        String[] whereArgs = {String.valueOf(idUsuario)};

        int rowsAffected = db.delete("Usuario", whereClause, whereArgs);

        db.close();

        return rowsAffected;
    }

    public int actualizarUsuario(User usuario) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("Username", usuario.getUsername());
        values.put("Password", usuario.getPassword());
        values.put("Nombre", usuario.getNombre());
        values.put("Apellidos", usuario.getApellidos());
        values.put("idRol", usuario.getIdRol());
        values.put("logeado", 0);

        String whereClause = "ID = ?";
        String[] whereArgs = {String.valueOf(usuario.getId())};

        int rowsAffected = db.update("Usuario", values, whereClause, whereArgs);

        db.close();

        return rowsAffected;
    }

    public List<User> listarUsuarios() {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                "ID",
                "Username",
                "Password",
                "Nombre",
                "Apellidos",
                "idRol",
        };

        Cursor cursor = db.query(
                "Usuario",
                projection,
                null,
                null,
                null,
                null,
                null
        );

        List<User> usuarios = new ArrayList<>();

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("ID"));
            String username = cursor.getString(cursor.getColumnIndexOrThrow("Username"));
            String password = cursor.getString(cursor.getColumnIndexOrThrow("Password"));
            String nombre = cursor.getString(cursor.getColumnIndexOrThrow("Nombre"));
            String apellidos = cursor.getString(cursor.getColumnIndexOrThrow("Apellidos"));
            int idRol = cursor.getInt(cursor.getColumnIndexOrThrow("idRol"));

            User usuario = new User(id, username, password, nombre, apellidos, idRol, 0);
            usuarios.add(usuario);
        }

        cursor.close();

        return usuarios;
    }

    @SuppressLint("Range")
    public User obtenerIdUsuarioLogueado() {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {"ID", "Username", "Password", "Nombre", "Apellidos", "idRol", "logeado"};


        String selection = "logeado = ?";
        String[] selectionArgs = {"1"};

        Cursor cursor = db.query(
                "Usuario",
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        User idUsuarioLogueado = null;
        if (cursor.moveToFirst()) {
            int userId = cursor.getInt(cursor.getColumnIndexOrThrow("ID"));
            String username = cursor.getString(cursor.getColumnIndexOrThrow("Username"));
            String password = cursor.getString(cursor.getColumnIndexOrThrow("Password"));
            String nombre = cursor.getString(cursor.getColumnIndexOrThrow("Nombre"));
            String apellidos = cursor.getString(cursor.getColumnIndexOrThrow("Apellidos"));
            int idRol = cursor.getInt(cursor.getColumnIndexOrThrow("idRol"));
            int isLogged = cursor.getInt(cursor.getColumnIndexOrThrow("logeado"));

            idUsuarioLogueado = new User(userId, username, password, nombre, apellidos, idRol, isLogged);
            idUsuarioLogueado.setLogeado(isLogged);
        }

        cursor.close();

        return idUsuarioLogueado;
    }


    public long crearCaso(Caso caso, int idAbogado) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("nombreCaso", caso.getNombreCaso());
        values.put("Descripcionbreve", caso.getDescripcionBreve());
        values.put("Estado", caso.getEstado());
        values.put("Idcliente", caso.getIdCliente());

        long casoId = db.insert("Casos", null, values);

        if (casoId != -1) {
            ContentValues abogadoCasoValues = new ContentValues();
            abogadoCasoValues.put("IDCaso", casoId);
            abogadoCasoValues.put("IDusuario", idAbogado);
            db.insert("AbogadoCaso", null, abogadoCasoValues);
        }

        db.close();

        return casoId;
    }

    public int actualizarCaso(Caso caso) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("nombreCaso", caso.getNombreCaso());
        values.put("Descripcionbreve", caso.getDescripcionBreve());
        values.put("Estado", caso.getEstado());
        values.put("Idcliente", caso.getIdCliente());

        int rowsAffected = db.update("Casos", values, "IDcaso = ?", new String[]{String.valueOf(caso.getIdCaso())});

        db.close();

        return rowsAffected;
    }

    public int eliminarCaso(int casoID) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete("AbogadoCaso", "IDCaso = ?", new String[]{String.valueOf(casoID)});
        int rowsAffected = db.delete("Casos", "IDcaso = ?", new String[]{String.valueOf(casoID)});

        db.close();

        return rowsAffected;
    }

    public List<Caso> listarCasosAbogado(int idAbogado) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT Casos.IDcaso, Casos.nombreCaso, Casos.Descripcionbreve, Casos.Estado, Casos.Idcliente " +
                "FROM Casos " +
                "INNER JOIN AbogadoCaso ON Casos.IDcaso = AbogadoCaso.IDCaso " +
                "WHERE AbogadoCaso.IDusuario = ?";

        String[] selectionArgs = {String.valueOf(idAbogado)};

        Cursor cursor = db.rawQuery(query, selectionArgs);

        List<Caso> casos = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                int idCaso = cursor.getInt(cursor.getColumnIndexOrThrow("IDcaso"));
                String nombreCaso = cursor.getString(cursor.getColumnIndexOrThrow("nombreCaso"));
                String descripcionCaso = cursor.getString(cursor.getColumnIndexOrThrow("Descripcionbreve"));
                String estadoCaso = cursor.getString(cursor.getColumnIndexOrThrow("Estado"));
                int idCliente = cursor.getInt(cursor.getColumnIndexOrThrow("Idcliente"));

                Caso caso = new Caso(idCaso, nombreCaso, descripcionCaso, estadoCaso, idCliente);
                casos.add(caso);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return casos;
    }

    public List<Caso> listarCasos() {
        List<Caso> casos = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + "Casos";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                int idCaso = cursor.getInt(cursor.getColumnIndexOrThrow("IDcaso"));
                String nombreCaso = cursor.getString(cursor.getColumnIndexOrThrow("nombreCaso"));
                String descripcionCaso = cursor.getString(cursor.getColumnIndexOrThrow("Descripcionbreve"));
                String estadoCaso = cursor.getString(cursor.getColumnIndexOrThrow("Estado"));
                int idCliente = cursor.getInt(cursor.getColumnIndexOrThrow("Idcliente"));

                Caso caso = new Caso(idCaso, nombreCaso, descripcionCaso, estadoCaso, idCliente);
                casos.add(caso);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return casos;
    }


    public List<User> obtenerClientes(int idRol) {
        List<User> clientes = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {"ID", "Username", "Password", "Nombre", "Apellidos", "idRol"};
        String selection = "idRol" + " = ?";
        String[] selectionArgs = {String.valueOf(idRol)};

        Cursor cursor = db.query("Usuario", columns, selection, selectionArgs, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("ID"));
                String username = cursor.getString(cursor.getColumnIndexOrThrow("Username"));
                String password = cursor.getString(cursor.getColumnIndexOrThrow("Password"));
                String nombre = cursor.getString(cursor.getColumnIndexOrThrow("Nombre"));
                String apellidos = cursor.getString(cursor.getColumnIndexOrThrow("Apellidos"));
                int idRolUsuario = cursor.getInt(cursor.getColumnIndexOrThrow("idRol"));

                User cliente = new User(id, username, password, nombre, apellidos, idRolUsuario, 0);
                clientes.add(cliente);
            } while (cursor.moveToNext());

            cursor.close();
        }

        return clientes;
    }

    public List<Caso> obtenerCasosPorCliente(int idCliente) {
        List<Caso> casos = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                "IDcaso",
                "nombreCaso",
                "Descripcionbreve",
                "Estado",
                "Idcliente"
        };

        String selection = "Idcliente = ?";
        String[] selectionArgs = {String.valueOf(idCliente)};

        Cursor cursor = db.query(
                "Casos",
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        while (cursor.moveToNext()) {
            int idCaso = cursor.getInt(cursor.getColumnIndexOrThrow("IDcaso"));
            String nombreCaso = cursor.getString(cursor.getColumnIndexOrThrow("nombreCaso"));
            String descripcionBreve = cursor.getString(cursor.getColumnIndexOrThrow("Descripcionbreve"));
            String estado = cursor.getString(cursor.getColumnIndexOrThrow("Estado"));

            Caso caso = new Caso(idCaso, nombreCaso, descripcionBreve, estado, idCliente);
            casos.add(caso);
        }

        cursor.close();
        db.close();

        return casos;
    }

    public List<User> obtenerClientesPorAbogado(int idUsuario) {
        SQLiteDatabase db = getReadableDatabase();

        String query = "SELECT DISTINCT U.ID, U.Username, U.Password, U.Nombre, U.Apellidos, U.idRol, U.logeado " +
                "FROM Usuario U " +
                "INNER JOIN Casos C ON U.ID = C.Idcliente " +
                "INNER JOIN AbogadoCaso AC ON C.IDcaso = AC.IDCaso " +
                "WHERE AC.IDusuario = ?";

        String[] selectionArgs = {String.valueOf(idUsuario)};
        Cursor cursor = db.rawQuery(query, selectionArgs);

        List<User> clientes = new ArrayList<>();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("ID"));
            String username = cursor.getString(cursor.getColumnIndexOrThrow("Username"));
            String password = cursor.getString(cursor.getColumnIndexOrThrow("Password"));
            String nombre = cursor.getString(cursor.getColumnIndexOrThrow("Nombre"));
            String apellidos = cursor.getString(cursor.getColumnIndexOrThrow("Apellidos"));
            int idRol = cursor.getInt(cursor.getColumnIndexOrThrow("idRol"));
            int isLoggeado = cursor.getInt(cursor.getColumnIndexOrThrow("logeado"));

            User cliente = new User(id, username, password, nombre, apellidos, idRol, isLoggeado);
            clientes.add(cliente);
        }

        cursor.close();
        db.close();

        return clientes;
    }

    public void cerrarSesion() {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("logeado", 0);

        String selection = "logeado = ?";
        String[] selectionArgs = {"1"};

        db.update("Usuario", values, selection, selectionArgs);

        db.close();
    }

    public List<Roles> obtenerRoles() {
        List<Roles> roles = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                "IDrol",
                "Rolname"
        };

        Cursor cursor = db.query(
                "Rol",
                projection,
                null,
                null,
                null,
                null,
                null
        );

        while (cursor.moveToNext()) {
            int idRol = cursor.getInt(cursor.getColumnIndexOrThrow("IDrol"));
            String nombre = cursor.getString(cursor.getColumnIndexOrThrow("Rolname"));

            Roles rol = new Roles(idRol, nombre);
            roles.add(rol);
        }

        cursor.close();

        return roles;
    }

    public void insertarRol(Roles rol) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("IDrol", rol.getIdRol());
        values.put("Rolname", rol.getRolname());

        db.insert("Rol", null, values);
    }

    public boolean insertReporte(Reporte reporte) {
        try (SQLiteDatabase db = getWritableDatabase()) {
            ContentValues values = new ContentValues();
            values.put("Periodo", reporte.getPeriodo());
            values.put("Descripcion", reporte.getDescripcion());
            values.put("Enero", reporte.getEnero());
            values.put("Febrero", reporte.getFebrero());
            values.put("Marzo", reporte.getMarzo());
            values.put("Abril", reporte.getAbril());
            values.put("Mayo", reporte.getMayo());
            values.put("Junio", reporte.getJunio());
            values.put("Julio", reporte.getJulio());
            values.put("Agosto", reporte.getAgosto());
            values.put("Septiembre", reporte.getSeptiembre());
            values.put("Octubre", reporte.getOctubre());
            values.put("Noviembre", reporte.getNoviembre());
            values.put("Diciembre", reporte.getDiciembre());

            long result = db.insert("Reportes", null, values);

            return result != -1;
        } catch (SQLiteException e) {
            Log.e("Database", "Error al insertar registro: " + e.getMessage());
            return false;
        }
    }

    public int eliminarReportes(int id) {
        try (SQLiteDatabase db = getWritableDatabase()) {
            String tableName = "Reportes";
            String whereClause = "ID = ?";
            String[] whereArgs = {String.valueOf(id)};
            int rowsAffected = db.delete(tableName, whereClause, whereArgs);
            return rowsAffected;

        } catch (SQLiteException e) {
            Log.i("Database", "Error al eliminar registro: " + e.getMessage());
            return 0;
        }
    }

    @SuppressLint("Range")
    public List<Reporte> obtenerReportes() {
        List<Reporte> reportes = new ArrayList<>();
        try (SQLiteDatabase db = getReadableDatabase()) {
            try (Cursor cursor = db.query("Reportes", null, null, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    do {
                        Reporte reporte = new Reporte();
                        reporte.setId(cursor.getInt(cursor.getColumnIndex("ID")));
                        reporte.setPeriodo(cursor.getString(cursor.getColumnIndex("Periodo")));
                        reporte.setDescripcion(cursor.getString(cursor.getColumnIndex("Descripcion")));
                        reporte.setEnero(cursor.getString(cursor.getColumnIndex("Enero")));
                        reporte.setFebrero(cursor.getString(cursor.getColumnIndex("Febrero")));
                        reporte.setMarzo(cursor.getString(cursor.getColumnIndex("Marzo")));
                        reporte.setAbril(cursor.getString(cursor.getColumnIndex("Abril")));
                        reporte.setMayo(cursor.getString(cursor.getColumnIndex("Mayo")));
                        reporte.setJunio(cursor.getString(cursor.getColumnIndex("Junio")));
                        reporte.setJulio(cursor.getString(cursor.getColumnIndex("Julio")));
                        reporte.setAgosto(cursor.getString(cursor.getColumnIndex("Agosto")));
                        reporte.setSeptiembre(cursor.getString(cursor.getColumnIndex("Septiembre")));
                        reporte.setOctubre(cursor.getString(cursor.getColumnIndex("Octubre")));
                        reporte.setNoviembre(cursor.getString(cursor.getColumnIndex("Noviembre")));
                        reporte.setDiciembre(cursor.getString(cursor.getColumnIndex("Diciembre")));
                        reportes.add(reporte);
                    } while (cursor.moveToNext());
                }
            }
        } catch (SQLiteException e) {
            Log.e("Database", "Error al obtener registros: " + e.getMessage());
        }
        return reportes;
    }

}








