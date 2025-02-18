package com.bartodelini.pixel.math.matrix;

import com.bartodelini.pixel.math.vector.Vector2f;

import java.util.Objects;

/**
 * A <i>Matrix2f</i> represents a 2x2 matrix with components of type {@code float}.
 *
 * @author Bartolini
 * @version 1.0
 */
public class Matrix2f {

    /**
     * The identity matrix.
     */
    public static final Matrix2f IDENTITY = new Matrix2f(
            1, 0,
            0, 1);

    /**
     * A matrix with every component set to 0.
     */
    public static final Matrix2f ZERO = new Matrix2f(0);

    /**
     * A matrix with every component set to 1.
     */
    public static final Matrix2f ONE = new Matrix2f(1);

    private final Vector2f v1;
    private final Vector2f v2;

    /**
     * Allocates a new {@code Matrix2f} object by passing in its two columns as
     * {@code Vector2f} objects.
     *
     * @param v1 the {@code Vector2f} used for the first column.
     * @param v2 the {@code Vector2f} used for the second column.
     * @throws NullPointerException if any of the specified columns is {@code null}.
     */
    public Matrix2f(Vector2f v1, Vector2f v2) {
        Objects.requireNonNull(v1, "v1 must not be null");
        Objects.requireNonNull(v2, "v2 must not be null");
        this.v1 = v1;
        this.v2 = v2;
    }

    /**
     * Allocates a new {@code Matrix2f} object by passing in all its components.
     * This constructor has the same effect as {@linkplain #Matrix2f(Vector2f, Vector2f)
     * Matrix2f} {@code (new Vector2f(m11, m21), new Vector2f(m12, m22))}.
     *
     * @param m11 the component of the first column and first row.
     * @param m12 the component of the second column and first row.
     * @param m21 the component of the first column and second row.
     * @param m22 the component of the second column and first row.
     */
    public Matrix2f(float m11, float m12,
                    float m21, float m22) {
        this(
                new Vector2f(m11, m21),
                new Vector2f(m12, m22));
    }

    /**
     * Allocates a new {@code Matrix2f} object by passing in a single component,
     * which will be used for all its components. This constructor has
     * the same effect as {@linkplain #Matrix2f(float, float, float, float) Matrix2f}
     * {@code (f, f, f, f)}.
     *
     * @param f the value used for all components.
     */
    public Matrix2f(float f) {
        this(
                f, f,
                f, f);
    }

    /**
     * Returns the first column of this matrix as a {@code Vector2f} object.
     *
     * @return the first column of this matrix.
     */
    public Vector2f getV1() {
        return v1;
    }

    /**
     * Returns the second column of this matrix as a {@code Vector2f} object.
     *
     * @return the second column of this matrix.
     */
    public Vector2f getV2() {
        return v2;
    }

    /**
     * Returns the sum of the current {@code Matrix2f} and the passed in {@code Matrix2f}.
     * <p>
     * Note: This method leaves the current {@code Matrix2f} instance unchanged.
     *
     * @param mat the {@code Matrix2f} to be added to the current {@code Matrix2f}.
     * @return the sum of the current {@code Matrix2f} and the passed in {@code Matrix2f}.
     * @throws NullPointerException if {@code mat} is {@code null}.
     */
    public Matrix2f add(Matrix2f mat) {
        Objects.requireNonNull(mat, "mat must not be null");
        return new Matrix2f(
                getV1().add(mat.getV1()),
                getV2().add(mat.getV2()));
    }

    /**
     * Returns the difference of the current {@code Matrix2f} and the passed in {@code Matrix2f}.
     * <p>
     * Note: This method leaves the current {@code Matrix2f} instance unchanged.
     *
     * @param mat the {@code Matrix2f} to be subtracted from the current {@code Matrix2f}.
     * @return the difference of the current {@code Matrix2f} and the passed in {@code Matrix2f}.
     * @throws NullPointerException if {@code mat} is {@code null}.
     */
    public Matrix2f subtract(Matrix2f mat) {
        Objects.requireNonNull(mat, "mat must not be null");
        return new Matrix2f(
                getV1().subtract(mat.getV1()),
                getV2().subtract(mat.getV2()));
    }

    /**
     * Returns a copy of the current {@code Matrix2f} with every component scaled by the
     * specified factor.
     * <p>
     * Note: This method leaves the current {@code Matrix2f} instance unchanged.
     *
     * @param factor the factor to scale the components of this {@code Matrix2f} by.
     * @return a copy of the current {@code Matrix2f} with every component scaled by the
     * specified factor.
     */
    public Matrix2f scale(float factor) {
        return new Matrix2f(
                getV1().scale(factor),
                getV2().scale(factor));
    }

    /**
     * Returns the matrix product of the current {@code Matrix2f} and the passed in {@code Matrix2f}.
     * <p>
     * Note: This method leaves the current {@code Matrix2f} instance unchanged.
     *
     * @param mat the {@code Matrix2f} to multiply the current {@code Matrix2f} with.
     * @return the matrix product of the current {@code Matrix2f} and the passed in {@code Matrix2f}.
     * @throws NullPointerException if {@code mat} is {@code null}.
     */
    public Matrix2f multiply(Matrix2f mat) {
        Objects.requireNonNull(mat, "mat must not be null");
        return new Matrix2f(
                getV1().getX() * mat.getV1().getX() + getV2().getX() * mat.getV1().getY(),
                getV1().getX() * mat.getV2().getX() + getV2().getX() * mat.getV2().getY(),
                getV1().getY() * mat.getV1().getX() + getV2().getY() * mat.getV1().getY(),
                getV1().getY() * mat.getV2().getX() + getV2().getY() * mat.getV2().getY());
//        return new Matrix2f(transpose.getV1().dot(mat.getV1()), transpose.getV1().dot(mat.getV2()),
//                transpose.getV2().dot(mat.getV1()), transpose.getV2().dot(mat.getV2()));
    }

    /**
     * Returns a copy of the passed in {@code Vector2f} multiplied by this {@code Matrix2f}.
     *
     * @param vec the {@code Vector2f} to be multiplied with this {@code Matrix2f}.
     * @return a copy of the passed in {@code Vector2f} multiplied by this {@code Matrix2f}.
     * @throws NullPointerException if {@code vec} is {@code null}.
     */
    public Vector2f multiply(Vector2f vec) {
        Objects.requireNonNull(vec, "vec must not be null");
        return new Vector2f(
                getV1().getX() * vec.getX() + getV2().getX() + vec.getY(),
                getV1().getY() * vec.getX() + getV2().getY() * vec.getY());
//        return new Vector2f(transpose.getV1().dot(vec), transpose.getV2().dot(vec));
    }

    /**
     * Returns the transpose of this {@code Matrix2f}.
     *
     * @return the transpose of this {@code Matrix2f}.
     */
    public Matrix2f transpose() {
        return new Matrix2f(
                getV1().getX(), getV1().getY(),
                getV2().getX(), getV2().getY());
    }

    /**
     * Returns the cofactor matrix of this {@code Matrix2f}.
     *
     * @return the cofactor matrix of this {@code Matrix2f}.
     */
    public Matrix2f cofactor() {
        return new Matrix2f(
                getV2().getY(), -getV1().getY(),
                -getV2().getX(), getV1().getX());
    }

    /**
     * Returns the adjugate matrix of this {@code Matrix2f}.
     *
     * @return the adjugate matrix of this {@code Matrix2f}.
     */
    public Matrix2f adjugate() {
        return cofactor().transpose();
    }

    /**
     * Returns the determinant of this {@code Matrix2f}.
     *
     * @return the determinant of this {@code Matrix2f}.
     */
    public float determinant() {
        return getV1().getX() * getV2().getY() - getV2().getX() * getV1().getY();
    }

    /**
     * Returns the inverse od this {@code Matrix2f}.
     *
     * @return the inverse od this {@code Matrix2f}.
     * @throws UnsupportedOperationException if the inverse does not exist;
     *                                       the determinant is equal to 0.
     */
    public Matrix2f inverse() {
        if (determinant() == 0) {
            throw new UnsupportedOperationException("The determinant of this matrix is equal to 0;" +
                    "the inverse does not exist.");
        }
        return adjugate().scale(1 / determinant());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Matrix2f matrix2f)) return false;
        return getV1().equals(matrix2f.getV1())
                && getV2().equals(matrix2f.getV2());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getV1(), getV2());
    }

    @Override
    public String toString() {
        return getV1().getX() + "  " + getV2().getX() + "\n" +
                getV1().getY() + "  " + getV2().getY();
    }
}