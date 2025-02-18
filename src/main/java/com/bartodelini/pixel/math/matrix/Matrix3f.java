package com.bartodelini.pixel.math.matrix;

import com.bartodelini.pixel.math.vector.Vector3f;

import java.util.Objects;

/**
 * A <i>Matrix3f</i> represents a 3x3 matrix with components of type {@code float}.
 *
 * @author Bartolini
 * @version 1.0
 */
public class Matrix3f {

    /**
     * The identity matrix.
     */
    public static final Matrix3f IDENTITY = new Matrix3f(
            1, 0, 0,
            0, 1, 0,
            0, 0, 1);

    /**
     * A matrix with every component set to 0.
     */
    public static final Matrix3f ZERO = new Matrix3f(0);

    /**
     * A matrix with every component set to 1.
     */
    public static final Matrix3f ONE = new Matrix3f(1);

    private final Vector3f v1;
    private final Vector3f v2;
    private final Vector3f v3;

    /**
     * Allocates a new {@code Matrix3f} object by passing in its three columns as
     * {@code Vector3f} objects.
     *
     * @param v1 the {@code Vector3f} used for the first column.
     * @param v2 the {@code Vector3f} used for the second column.
     * @param v3 the {@code Vector3f} used for the third column.
     * @throws NullPointerException if any of the specified columns is {@code null}.
     */
    public Matrix3f(Vector3f v1, Vector3f v2, Vector3f v3) {
        Objects.requireNonNull(v1, "v1 must not be null");
        Objects.requireNonNull(v2, "v2 must not be null");
        Objects.requireNonNull(v3, "v3 must not be null");
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
    }

    /**
     * Allocates a new {@code Matrix3f} object by passing in all its components.
     * This constructor has the same effect as
     * {@linkplain #Matrix3f(Vector3f, Vector3f, Vector3f) Matrix3f}
     * {@code (new Vector3f(m11, m12, m13), new Vector3f(m21, m22, m23),
     * new Vector3f(m31, m32, m33))}.
     *
     * @param m11 the component of the first column and first row.
     * @param m12 the component of the second column and first row.
     * @param m13 the component of the third column and first row.
     * @param m21 the component of the first column and second row.
     * @param m22 the component of the second column and second row.
     * @param m23 the component of the third column and second row.
     * @param m31 the component of the first column and third row.
     * @param m32 the component of the second column and third row.
     * @param m33 the component of the third column and third row.
     */
    public Matrix3f(float m11, float m12, float m13,
                    float m21, float m22, float m23,
                    float m31, float m32, float m33) {
        this(
                new Vector3f(m11, m21, m31),
                new Vector3f(m12, m22, m32),
                new Vector3f(m13, m23, m33));
    }

    /**
     * Allocates a new {@code Matrix3f} object by passing in a single component,
     * which will be used for all its components. This constructor has
     * the same effect as {@linkplain #Matrix3f(float, float, float, float, float, float, float, float, float) Matrix3f}
     * {@code (f, f, f, f, f, f, f, f, f)}.
     *
     * @param f the value used for all components.
     */
    public Matrix3f(float f) {
        this(
                f, f, f,
                f, f, f,
                f, f, f);
    }

    /**
     * Returns the first column of this matrix as a {@code Vector3f} object.
     *
     * @return the first column of this matrix.
     */
    public Vector3f getV1() {
        return v1;
    }

    /**
     * Returns the second column of this matrix as a {@code Vector3f} object.
     *
     * @return the second column of this matrix.
     */
    public Vector3f getV2() {
        return v2;
    }

    /**
     * Returns the third column of this matrix as a {@code Vector3f} object.
     *
     * @return the third column of this matrix.
     */
    public Vector3f getV3() {
        return v3;
    }

    /**
     * Returns the sum of the current {@code Matrix3f} and the passed in {@code Matrix3f}.
     * <p>
     * Note: This method leaves the current {@code Matrix3f} instance unchanged.
     *
     * @param mat the {@code Matrix3f} to be added to the current {@code Matrix3f}.
     * @return the sum of the current {@code Matrix3f} and the passed in {@code Matrix3f}.
     * @throws NullPointerException if {@code mat} is {@code null}.
     */
    public Matrix3f add(Matrix3f mat) {
        Objects.requireNonNull(mat, "mat must not be null");
        return new Matrix3f(
                getV1().add(mat.getV1()),
                getV2().add(mat.getV2()),
                getV3().add(mat.getV3()));
    }

    /**
     * Returns the difference of the current {@code Matrix3f} and the passed in {@code Matrix3f}.
     * <p>
     * Note: This method leaves the current {@code Matrix3f} instance unchanged.
     *
     * @param mat the {@code Matrix3f} to be subtracted from the current {@code Matrix3f}.
     * @return the difference of the current {@code Matrix3f} and the passed in {@code Matrix3f}.
     * @throws NullPointerException if {@code mat} is {@code null}.
     */
    public Matrix3f subtract(Matrix3f mat) {
        Objects.requireNonNull(mat, "mat must not be null");
        return new Matrix3f(
                getV1().subtract(mat.getV1()),
                getV2().subtract(mat.getV2()),
                getV3().subtract(mat.getV3()));
    }

    /**
     * Returns a copy of the current {@code Matrix3f} with every component scaled by the
     * specified factor.
     * <p>
     * Note: This method leaves the current {@code Matrix3f} instance unchanged.
     *
     * @param factor the factor to scale the components of this {@code Matrix3f} by.
     * @return a copy of the current {@code Matrix3f} with every component scaled by the
     * specified factor.
     */
    public Matrix3f scale(float factor) {
        return new Matrix3f(
                getV1().scale(factor),
                getV2().scale(factor),
                getV3().scale(factor));
    }

    /**
     * Returns the matrix product of the current {@code Matrix3f} and the passed in {@code Matrix3f}.
     * <p>
     * Note: This method leaves the current {@code Matrix3f} instance unchanged.
     *
     * @param mat the {@code Matrix3f} to multiply the current {@code Matrix3f} with.
     * @return the matrix product of the current {@code Matrix3f} and the passed in {@code Matrix3f}.
     * @throws NullPointerException if {@code mat} is {@code null}.
     */
    public Matrix3f multiply(Matrix3f mat) {
        Objects.requireNonNull(mat, "mat must not be null");
        return new Matrix3f(
                getV1().getX() * mat.getV1().getX() + getV2().getX() * mat.getV1().getY()
                        + getV3().getX() * mat.getV1().getZ(),
                getV1().getX() * mat.getV2().getX() + getV2().getX() * mat.getV2().getY()
                        + getV3().getX() * mat.getV2().getZ(),
                getV1().getX() * mat.getV3().getX() + getV2().getX() * mat.getV3().getY()
                        + getV3().getX() * mat.getV3().getZ(),

                getV1().getY() * mat.getV1().getX() + getV2().getY() * mat.getV1().getY()
                        + getV3().getY() * mat.getV1().getZ(),
                getV1().getY() * mat.getV2().getX() + getV2().getY() * mat.getV2().getY()
                        + getV3().getY() * mat.getV2().getZ(),
                getV1().getY() * mat.getV3().getX() + getV2().getY() * mat.getV3().getY()
                        + getV3().getY() * mat.getV3().getZ(),

                getV1().getZ() * mat.getV1().getX() + getV2().getZ() * mat.getV1().getY()
                        + getV3().getZ() * mat.getV1().getZ(),
                getV1().getZ() * mat.getV2().getX() + getV2().getZ() * mat.getV2().getY()
                        + getV3().getZ() * mat.getV2().getZ(),
                getV1().getZ() * mat.getV3().getX() + getV2().getZ() * mat.getV3().getY()
                        + getV3().getZ() * mat.getV3().getZ());
//        Matrix3f t = transpose();
//        return new Matrix3f(
//                t.getV1().dot(mat.getV1()), t.getV1().dot(mat.getV2()), t.getV1().dot(mat.getV3()),
//                t.getV2().dot(mat.getV1()), t.getV2().dot(mat.getV2()), t.getV2().dot(mat.getV3()),
//                t.getV3().dot(mat.getV1()), t.getV3().dot(mat.getV2()), t.getV3().dot(mat.getV3()));
    }

    /**
     * Returns a copy of the passed in {@code Vector3f} multiplied by this {@code Matrix3f}.
     *
     * @param vec the {@code Vector3f} to be multiplied with this {@code Matrix3f}.
     * @return a copy of the passed in {@code Vector3f} multiplied by this {@code Matrix3f}.
     * @throws NullPointerException if {@code vec} is {@code null}.
     */
    public Vector3f multiply(Vector3f vec) {
        Objects.requireNonNull(vec, "vec must not be null");
        return new Vector3f(
                getV1().getX() * vec.getX() + getV2().getX() * vec.getY() + getV3().getX() * vec.getZ(),
                getV1().getY() * vec.getX() + getV2().getY() * vec.getY() + getV3().getY() * vec.getZ(),
                getV1().getZ() * vec.getX() + getV2().getZ() * vec.getY() + getV3().getZ() * vec.getZ());
//        Matrix3f t = transpose();
//        return new Vector3f(t.getV1().dot(vec), t.getV2().dot(vec), t.getV3().dot(vec));
    }

    /**
     * Returns the transpose of this {@code Matrix3f}.
     *
     * @return the transpose of this {@code Matrix3f}.
     */
    public Matrix3f transpose() {
        return new Matrix3f(
                getV1().getX(), getV1().getY(), getV1().getZ(),
                getV2().getX(), getV2().getY(), getV2().getZ(),
                getV3().getX(), getV3().getY(), getV3().getZ());
    }

    /**
     * Returns the cofactor matrix of this {@code Matrix3f}.
     *
     * @return the cofactor matrix of this {@code Matrix3f}.
     */
    public Matrix3f cofactor() {
        return new Matrix3f(
                +new Matrix2f(getV2().getY(), getV3().getY(), getV2().getZ(), getV3().getZ()).determinant(),
                -new Matrix2f(getV1().getY(), getV3().getY(), getV1().getZ(), getV3().getZ()).determinant(),
                +new Matrix2f(getV1().getY(), getV2().getY(), getV1().getZ(), getV2().getZ()).determinant(),

                -new Matrix2f(getV2().getX(), getV3().getX(), getV2().getZ(), getV3().getZ()).determinant(),
                +new Matrix2f(getV1().getX(), getV3().getX(), getV1().getZ(), getV3().getZ()).determinant(),
                -new Matrix2f(getV1().getX(), getV2().getX(), getV1().getZ(), getV2().getZ()).determinant(),

                +new Matrix2f(getV2().getX(), getV3().getX(), getV2().getY(), getV3().getY()).determinant(),
                -new Matrix2f(getV1().getX(), getV3().getX(), getV1().getY(), getV3().getY()).determinant(),
                +new Matrix2f(getV1().getX(), getV2().getX(), getV1().getY(), getV2().getY()).determinant());
    }

    /**
     * Returns the adjugate matrix of this {@code Matrix3f}.
     *
     * @return the adjugate matrix of this {@code Matrix3f}.
     */
    public Matrix3f adjugate() {
        return cofactor().transpose();
    }

    /**
     * Returns the determinant of this {@code Matrix3f}.
     *
     * @return the determinant of this {@code Matrix3f}.
     */
    public float determinant() {
        return getV1().getX() * getV2().getY() * getV3().getZ() +
                getV2().getX() * getV3().getY() * getV1().getZ() +
                getV3().getX() * getV1().getY() * getV2().getZ() -
                getV1().getX() * getV3().getY() * getV2().getZ() -
                getV2().getX() * getV1().getY() * getV3().getZ() -
                getV3().getX() * getV2().getY() * getV1().getZ();
    }

    /**
     * Returns the inverse od this {@code Matrix3f}.
     *
     * @return the inverse od this {@code Matrix3f}.
     * @throws UnsupportedOperationException if the inverse does not exist;
     *                                       the determinant is equal to 0.
     */
    public Matrix3f inverse() {
        if (determinant() == 0) {
            throw new UnsupportedOperationException("The determinant of this matrix is equal to 0;" +
                    "the inverse does not exist.");
        }
        return adjugate().scale(1 / determinant());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Matrix3f matrix3f)) return false;
        return getV1().equals(matrix3f.getV1())
                && getV2().equals(matrix3f.getV2())
                && getV3().equals(matrix3f.getV3());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getV1(), getV2(), getV3());
    }

    @Override
    public String toString() {
        return getV1().getX() + "  " + getV2().getX() + "  " + getV3().getX() + "\n" +
                getV1().getY() + "  " + getV2().getY() + "  " + getV3().getY() + "\n" +
                getV1().getZ() + "  " + getV2().getZ() + "  " + getV3().getZ();
    }
}