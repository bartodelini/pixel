package com.bartodelini.pixel.math.matrix;

import com.bartodelini.pixel.math.vector.Vector3f;
import com.bartodelini.pixel.math.vector.Vector4f;

import java.util.Objects;

/**
 * A <i>Matrix4f</i> represents a 4x4 matrix with components of type {@code float}.
 *
 * @author Bartolini
 * @version 1.0
 */
public class Matrix4f {

    /**
     * The identity matrix.
     */
    public static final Matrix4f IDENTITY = new Matrix4f(
            1, 0, 0, 0,
            0, 1, 0, 0,
            0, 0, 1, 0,
            0, 0, 0, 1);

    /**
     * A matrix with every component set to 0.
     */
    public static final Matrix4f ZERO = new Matrix4f(0);

    /**
     * A matrix with every component set to 1.
     */
    public static final Matrix4f ONE = new Matrix4f(1);

    private final Vector4f v1;
    private final Vector4f v2;
    private final Vector4f v3;
    private final Vector4f v4;

    /**
     * Allocates a new {@code Matrix4f} object by passing in its four columns as
     * {@code Vector4f} objects.
     *
     * @param v1 the {@code Vector4f} used for the first column.
     * @param v2 the {@code Vector4f} used for the second column.
     * @param v3 the {@code Vector4f} used for the third column.
     * @param v4 the {@code Vector4f} used for the fourth column.
     * @throws NullPointerException if any of the specified columns is {@code null}.
     */
    public Matrix4f(Vector4f v1, Vector4f v2, Vector4f v3, Vector4f v4) {
        Objects.requireNonNull(v1, "v1 must not be null");
        Objects.requireNonNull(v2, "v2 must not be null");
        Objects.requireNonNull(v3, "v3 must not be null");
        Objects.requireNonNull(v4, "v4 must not be null");
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
        this.v4 = v4;
    }

    /**
     * Allocates a new {@code Matrix4f} object by passing in all its components.
     * This constructor has the same effect as
     * {@linkplain #Matrix4f(Vector4f, Vector4f, Vector4f, Vector4f) Matrix4f}
     * {@code (new Vector4f(m11, m12, m13, m14), new Vector4f(m21, m22, m23, m24),
     * new Vector4f(m31, m32, m33, m34), new Vector4f(m41, m42, m43, m44))}.
     *
     * @param m11 the component of the first column and first row.
     * @param m12 the component of the second column and first row.
     * @param m13 the component of the third column and first row.
     * @param m14 the component of the fourth column and first row.
     * @param m21 the component of the first column and second row.
     * @param m22 the component of the second column and second row.
     * @param m23 the component of the third column and second row.
     * @param m24 the component of the fourth column and second row.
     * @param m31 the component of the first column and third row.
     * @param m32 the component of the second column and third row.
     * @param m33 the component of the third column and third row.
     * @param m34 the component of the fourth column and third row.
     * @param m41 the component of the first column and fourth row.
     * @param m42 the component of the second column and fourth row.
     * @param m43 the component of the third column and fourth row.
     * @param m44 the component of the fourth column and fourth row.
     */
    public Matrix4f(float m11, float m12, float m13, float m14,
                    float m21, float m22, float m23, float m24,
                    float m31, float m32, float m33, float m34,
                    float m41, float m42, float m43, float m44) {
        this(
                new Vector4f(m11, m21, m31, m41),
                new Vector4f(m12, m22, m32, m42),
                new Vector4f(m13, m23, m33, m43),
                new Vector4f(m14, m24, m34, m44));
    }

    /**
     * Allocates a new {@code Matrix4f} object by passing in a single component,
     * which will be used for all its components. This constructor has
     * the same effect as
     * {@linkplain #Matrix4f(float, float, float, float, float, float, float, float, float, float, float, float, float,
     * float, float, float) Matrix4f}
     * {@code (f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f)}.
     *
     * @param f the value used for all components.
     */
    public Matrix4f(float f) {
        this(
                f, f, f, f,
                f, f, f, f,
                f, f, f, f,
                f, f, f, f);
    }

    /**
     * Returns the first column of this matrix as a {@code Vector4f} object.
     *
     * @return the first column of this matrix.
     */
    public Vector4f getV1() {
        return v1;
    }

    /**
     * Returns the second column of this matrix as a {@code Vector4f} object.
     *
     * @return the second column of this matrix.
     */
    public Vector4f getV2() {
        return v2;
    }

    /**
     * Returns the third column of this matrix as a {@code Vector4f} object.
     *
     * @return the third column of this matrix.
     */
    public Vector4f getV3() {
        return v3;
    }

    /**
     * Returns the fourth column of this matrix as a {@code Vector4f} object.
     *
     * @return the fourth column of this matrix.
     */
    public Vector4f getV4() {
        return v4;
    }

    /**
     * Returns the sum of the current {@code Matrix4f} and the passed in {@code Matrix4f}.
     * <p>
     * Note: This method leaves the current {@code Matrix4f} instance unchanged.
     *
     * @param mat the {@code Matrix4f} to be added to the current {@code Matrix4f}.
     * @return the sum of the current {@code Matrix4f} and the passed in {@code Matrix4f}.
     * @throws NullPointerException if {@code mat} is {@code null}.
     */
    public Matrix4f add(Matrix4f mat) {
        Objects.requireNonNull(mat, "mat must not be null");
        return new Matrix4f(
                getV1().add(mat.getV1()),
                getV2().add(mat.getV2()),
                getV3().add(mat.getV3()),
                getV4().add(mat.getV4()));
    }

    /**
     * Returns the difference of the current {@code Matrix4f} and the passed in {@code Matrix4f}.
     * <p>
     * Note: This method leaves the current {@code Matrix4f} instance unchanged.
     *
     * @param mat the {@code Matrix4f} to be subtracted from the current {@code Matrix4f}.
     * @return the difference of the current {@code Matrix4f} and the passed in {@code Matrix4f}.
     * @throws NullPointerException if {@code mat} is {@code null}.
     */
    public Matrix4f subtract(Matrix4f mat) {
        Objects.requireNonNull(mat, "mat must not be null");
        return new Matrix4f(
                getV1().subtract(mat.getV1()),
                getV2().subtract(mat.getV2()),
                getV3().subtract(mat.getV3()),
                getV4().subtract(mat.getV4()));
    }

    /**
     * Returns a copy of the current {@code Matrix4f} with every component scaled by the
     * specified factor.
     * <p>
     * Note: This method leaves the current {@code Matrix4f} instance unchanged.
     *
     * @param factor the factor to scale the components of this {@code Matrix4f} by.
     * @return a copy of the current {@code Matrix4f} with every component scaled by the
     * specified factor.
     */
    public Matrix4f scale(float factor) {
        return new Matrix4f(
                getV1().scale(factor),
                getV2().scale(factor),
                getV3().scale(factor),
                getV4().scale(factor));
    }

    /**
     * Returns a copy of the current {@code Matrix4f} multiplied with the scaling matrix
     * consisting of a diagonal of the passed in {@code Vector3f} components.
     * <p>
     * Note: This method leaves the current {@code Matrix4f} instance unchanged.
     *
     * @param factors the {@code Vector3f} to construct the scaling matrix with.
     * @return a copy of the current {@code Matrix4f} multiplied with the scaling matrix
     * consisting of a diagonal of the passed in {@code Vector3f} components.
     */
    public Matrix4f scale(Vector3f factors) {
        return multiply(new Matrix4f(
                factors.getX(), 0, 0, 0,
                0, factors.getY(), 0, 0,
                0, 0, factors.getZ(), 0,
                0, 0, 0, 1));
    }

    /**
     * Returns the matrix product of the current {@code Matrix4f} and the passed in {@code Matrix4f}.
     * <p>
     * Note: This method leaves the current {@code Matrix4f} instance unchanged.
     *
     * @param mat the {@code Matrix4f} to multiply the current {@code Matrix4f} with.
     * @return the matrix product of the current {@code Matrix4f} and the passed in {@code Matrix4f}.
     * @throws NullPointerException if {@code mat} is {@code null}.
     */
    public Matrix4f multiply(Matrix4f mat) {
        Objects.requireNonNull(mat, "mat must not be null");
        return new Matrix4f(
                getV1().getX() * mat.getV1().getX() + getV2().getX() * mat.getV1().getY()
                        + getV3().getX() * mat.getV1().getZ() + getV4().getX() * mat.getV1().getW(),
                getV1().getX() * mat.getV2().getX() + getV2().getX() * mat.getV2().getY()
                        + getV3().getX() * mat.getV2().getZ() + getV4().getX() * mat.getV2().getW(),
                getV1().getX() * mat.getV3().getX() + getV2().getX() * mat.getV3().getY()
                        + getV3().getX() * mat.getV3().getZ() + getV4().getX() * mat.getV3().getW(),
                getV1().getX() * mat.getV4().getX() + getV2().getX() * mat.getV4().getY()
                        + getV3().getX() * mat.getV4().getZ() + getV4().getX() * mat.getV4().getW(),

                getV1().getY() * mat.getV1().getX() + getV2().getY() * mat.getV1().getY()
                        + getV3().getY() * mat.getV1().getZ() + getV4().getY() * mat.getV1().getW(),
                getV1().getY() * mat.getV2().getX() + getV2().getY() * mat.getV2().getY()
                        + getV3().getY() * mat.getV2().getZ() + getV4().getY() * mat.getV2().getW(),
                getV1().getY() * mat.getV3().getX() + getV2().getY() * mat.getV3().getY()
                        + getV3().getY() * mat.getV3().getZ() + getV4().getY() * mat.getV3().getW(),
                getV1().getY() * mat.getV4().getX() + getV2().getY() * mat.getV4().getY()
                        + getV3().getY() * mat.getV4().getZ() + getV4().getY() * mat.getV4().getW(),

                getV1().getZ() * mat.getV1().getX() + getV2().getZ() * mat.getV1().getY()
                        + getV3().getZ() * mat.getV1().getZ() + getV4().getZ() * mat.getV1().getW(),
                getV1().getZ() * mat.getV2().getX() + getV2().getZ() * mat.getV2().getY()
                        + getV3().getZ() * mat.getV2().getZ() + getV4().getZ() * mat.getV2().getW(),
                getV1().getZ() * mat.getV3().getX() + getV2().getZ() * mat.getV3().getY()
                        + getV3().getZ() * mat.getV3().getZ() + getV4().getZ() * mat.getV3().getW(),
                getV1().getZ() * mat.getV4().getX() + getV2().getZ() * mat.getV4().getY()
                        + getV3().getZ() * mat.getV4().getZ() + getV4().getZ() * mat.getV4().getW(),

                getV1().getW() * mat.getV1().getX() + getV2().getW() * mat.getV1().getY()
                        + getV3().getW() * mat.getV1().getZ() + getV4().getW() * mat.getV1().getW(),
                getV1().getW() * mat.getV2().getX() + getV2().getW() * mat.getV2().getY()
                        + getV3().getW() * mat.getV2().getZ() + getV4().getW() * mat.getV2().getW(),
                getV1().getW() * mat.getV3().getX() + getV2().getW() * mat.getV3().getY()
                        + getV3().getW() * mat.getV3().getZ() + getV4().getW() * mat.getV3().getW(),
                getV1().getW() * mat.getV4().getX() + getV2().getW() * mat.getV4().getY()
                        + getV3().getW() * mat.getV4().getZ() + getV4().getW() * mat.getV4().getW());
//        Matrix4f t = transpose();
//        return new Matrix4f(
//                t.getV1().dot(mat.getV1()), t.getV1().dot(mat.getV2()),
//                t.getV1().dot(mat.getV3()), t.getV1().dot(mat.getV4()),
//
//                t.getV2().dot(mat.getV1()), t.getV2().dot(mat.getV2()),
//                t.getV2().dot(mat.getV3()), t.getV2().dot(mat.getV4()),
//
//                t.getV3().dot(mat.getV1()), t.getV3().dot(mat.getV2()),
//                t.getV3().dot(mat.getV3()), t.getV3().dot(mat.getV4()),
//
//                t.getV4().dot(mat.getV1()), t.getV4().dot(mat.getV2()),
//                t.getV4().dot(mat.getV3()), t.getV4().dot(mat.getV4()));
    }

    /**
     * Returns a copy of the passed in {@code Vector4f} multiplied by this {@code Matrix4f}.
     *
     * @param vec the {@code Vector4f} to be multiplied with this {@code Matrix4f}.
     * @return a copy of the passed {@code Vector4f} multiplied by this {@code Matrix4f}.
     * @throws NullPointerException if {@code vec} is {@code null}.
     */
    public Vector4f multiply(Vector4f vec) {
        Objects.requireNonNull(vec, "vec must not be null");
        return new Vector4f(
                getV1().getX() * vec.getX() + getV2().getX() * vec.getY()
                        + getV3().getX() * vec.getZ() + getV4().getX() * vec.getW(),
                getV1().getY() * vec.getX() + getV2().getY() * vec.getY()
                        + getV3().getY() * vec.getZ() + getV4().getY() * vec.getW(),
                getV1().getZ() * vec.getX() + getV2().getZ() * vec.getY()
                        + getV3().getZ() * vec.getZ() + getV4().getZ() * vec.getW(),
                getV1().getW() * vec.getX() + getV2().getW() * vec.getY()
                        + getV3().getW() * vec.getZ() + getV4().getW() * vec.getW());
//        Matrix4f t = transpose();
//        return new Vector4f(t.getV1().dot(vec), t.getV2().dot(vec), t.getV3().dot(vec), t.getV4().dot(vec));
    }

    /**
     * Returns the result of the matrix vector multiplication of this {@code Matrix4f} and the
     * passed in {@code Vector3f} interpreted as a {@code Vector4f} with w as its last component.
     *
     * @param vec the {@code Vector3f} to be multiplied with this {@code Matrix4f}.
     * @param w   the component used for the {@code Vector4f} extension of the passed in vector.
     * @return a copy of the passed in {@code Vector3f} multiplied by this {@code Matrix4f}, with
     * w as its last component.
     */
    public Vector4f multiply(Vector3f vec, float w) {
        return multiply(new Vector4f(vec, w));
    }

    /**
     * Returns the result of the matrix vector multiplication of this {@code Matrix4f} and the
     * passed in {@code Vector3f} interpreted as a {@code Vector4f} with 1 as its last component.
     * This method has the same effect as
     * {@linkplain #multiply(Vector3f, float) multiply} {@code (vec, 1)}
     *
     * @param vec the {@code Vector3f} to be multiplied with this {@code Matrix4f}.
     * @return a copy of the passed in {@code Vector3f} multiplied by this {@code Matrix4f}, with
     * 1 as its last component.
     */
    public Vector4f multiply(Vector3f vec) {
        return multiply(vec, 1);
    }

    /**
     * Returns a copy of the current {@code Matrix4f} rotated by the specified angle around
     * the passed in axis.
     *
     * @param alpha the angle in degrees to rotate the {@code Matrix4f} by.
     * @param axis  the axis to rotate the {@code Matrix4f} around.
     * @return the {@code Matrix4f} rotated by the specified angle around
     * the passed in axis.
     */
    public Matrix4f rotate(float alpha, Vector3f axis) {
        Vector3f an = axis.normalize();
        float sin = (float) Math.sin(alpha);
        float cos = (float) Math.cos(alpha);
        return multiply(new Matrix4f(
                cos + an.getX() * an.getX() * (1 - cos),
                an.getX() * an.getY() * (1 - cos) - an.getZ() * sin,
                an.getX() * an.getZ() * (1 - cos) + an.getY() * sin,
                0,

                an.getY() * an.getX() * (1 - cos) + an.getZ() * sin,
                cos + an.getY() * an.getY() * (1 - cos),
                an.getY() * an.getZ() * (1 - cos) - an.getX() * sin,
                0,

                an.getZ() * an.getX() * (1 - cos) - an.getY() * sin,
                an.getZ() * an.getY() * (1 - cos) + an.getX() * sin,
                cos + an.getZ() * an.getZ() * (1 - cos),
                0,

                0, 0, 0, 1));
    }

    /**
     * Returns a copy of the current {@code Matrix4f} translated by the passed in
     * {@code Vector3f}.
     *
     * @param translation the {@code Vector3f} to translate the matrix by.
     * @return the {@code Matrix4f} translated by the passed in {@code Vector3f}.
     */
    public Matrix4f translate(Vector3f translation) {
        return multiply(new Matrix4f(
                1, 0, 0, translation.getX(),
                0, 1, 0, translation.getY(),
                0, 0, 1, translation.getZ(),
                0, 0, 0, 1));
    }

    /**
     * Returns the transpose of this {@code Matrix4f}.
     *
     * @return the transpose of this {@code Matrix4f}.
     */
    public Matrix4f transpose() {
        return new Matrix4f(
                getV1().getX(), getV1().getY(), getV1().getZ(), getV1().getW(),
                getV2().getX(), getV2().getY(), getV2().getZ(), getV2().getW(),
                getV3().getX(), getV3().getY(), getV3().getZ(), getV3().getW(),
                getV4().getX(), getV4().getY(), getV4().getZ(), getV4().getW());
    }

    /**
     * Returns the cofactor matrix of this {@code Matrix4f}.
     *
     * @return the cofactor matrix of this {@code Matrix4f}.
     */
    public Matrix4f cofactor() {
        return new Matrix4f(
                +new Matrix3f(
                        getV2().getY(), getV3().getY(), getV4().getY(),
                        getV2().getZ(), getV3().getZ(), getV4().getZ(),
                        getV2().getW(), getV3().getW(), getV4().getW()).determinant(),
                -new Matrix3f(
                        getV1().getY(), getV3().getY(), getV4().getY(),
                        getV1().getZ(), getV3().getZ(), getV4().getZ(),
                        getV1().getW(), getV3().getW(), getV4().getW()).determinant(),
                +new Matrix3f(
                        getV1().getY(), getV2().getY(), getV4().getY(),
                        getV1().getZ(), getV2().getZ(), getV4().getZ(),
                        getV1().getW(), getV2().getW(), getV4().getW()).determinant(),
                -new Matrix3f(
                        getV1().getY(), getV2().getY(), getV3().getY(),
                        getV1().getZ(), getV2().getZ(), getV3().getZ(),
                        getV1().getW(), getV2().getW(), getV3().getW()).determinant(),

                -new Matrix3f(
                        getV2().getX(), getV3().getX(), getV4().getX(),
                        getV2().getZ(), getV3().getZ(), getV4().getZ(),
                        getV2().getW(), getV3().getW(), getV4().getW()).determinant(),
                +new Matrix3f(
                        getV1().getX(), getV3().getX(), getV4().getX(),
                        getV1().getZ(), getV3().getZ(), getV4().getZ(),
                        getV1().getW(), getV3().getW(), getV4().getW()).determinant(),
                -new Matrix3f(
                        getV1().getX(), getV2().getX(), getV4().getX(),
                        getV1().getZ(), getV2().getZ(), getV4().getZ(),
                        getV1().getW(), getV2().getW(), getV4().getW()).determinant(),
                +new Matrix3f(
                        getV1().getX(), getV2().getX(), getV3().getX(),
                        getV1().getZ(), getV2().getZ(), getV3().getZ(),
                        getV1().getW(), getV2().getW(), getV3().getW()).determinant(),

                +new Matrix3f(
                        getV2().getX(), getV3().getX(), getV4().getX(),
                        getV2().getY(), getV3().getY(), getV4().getY(),
                        getV2().getW(), getV3().getW(), getV4().getW()).determinant(),
                -new Matrix3f(
                        getV1().getX(), getV3().getX(), getV4().getX(),
                        getV1().getY(), getV3().getY(), getV4().getY(),
                        getV1().getW(), getV3().getW(), getV4().getW()).determinant(),
                +new Matrix3f(
                        getV1().getX(), getV2().getX(), getV4().getX(),
                        getV1().getY(), getV2().getY(), getV4().getY(),
                        getV1().getW(), getV2().getW(), getV4().getW()).determinant(),
                -new Matrix3f(
                        getV1().getX(), getV2().getX(), getV3().getX(),
                        getV1().getY(), getV2().getY(), getV3().getY(),
                        getV1().getW(), getV2().getW(), getV3().getW()).determinant(),

                -new Matrix3f(
                        getV2().getX(), getV3().getX(), getV4().getX(),
                        getV2().getY(), getV3().getY(), getV4().getY(),
                        getV2().getZ(), getV3().getZ(), getV4().getZ()).determinant(),
                +new Matrix3f(
                        getV1().getX(), getV3().getX(), getV4().getX(),
                        getV1().getY(), getV3().getY(), getV4().getY(),
                        getV1().getZ(), getV3().getZ(), getV4().getZ()).determinant(),
                -new Matrix3f(
                        getV1().getX(), getV2().getX(), getV4().getX(),
                        getV1().getY(), getV2().getY(), getV4().getY(),
                        getV1().getZ(), getV2().getZ(), getV4().getZ()).determinant(),
                +new Matrix3f(
                        getV1().getX(), getV2().getX(), getV3().getX(),
                        getV1().getY(), getV2().getY(), getV3().getY(),
                        getV1().getZ(), getV2().getZ(), getV3().getZ()).determinant());
    }

    /**
     * Returns the adjugate matrix of this {@code Matrix4f}.
     *
     * @return the adjugate matrix of this {@code Matrix4f}.
     */
    public Matrix4f adjugate() {
        return cofactor().transpose();
    }

    /**
     * Returns the determinant of this {@code Matrix4f}.
     *
     * @return the determinant of this {@code Matrix4f}.
     */
    public float determinant() {
        return getV1().getX() * new Matrix3f(
                getV2().getY(), getV3().getY(), getV4().getY(),
                getV2().getZ(), getV3().getZ(), getV4().getZ(),
                getV2().getW(), getV3().getW(), getV4().getW()).determinant() -

                getV1().getY() * new Matrix3f(
                        getV2().getX(), getV3().getX(), getV4().getX(),
                        getV2().getZ(), getV3().getZ(), getV4().getZ(),
                        getV2().getW(), getV3().getW(), getV4().getW()).determinant() +

                getV1().getZ() * new Matrix3f(
                        getV2().getX(), getV3().getX(), getV4().getX(),
                        getV2().getY(), getV3().getY(), getV4().getY(),
                        getV2().getW(), getV3().getW(), getV4().getW()).determinant() -

                getV1().getW() * new Matrix3f(
                        getV2().getX(), getV3().getX(), getV4().getX(),
                        getV2().getY(), getV3().getY(), getV4().getY(),
                        getV2().getZ(), getV3().getZ(), getV4().getZ()).determinant();
    }

    /**
     * Returns the inverse od this {@code Matrix4f}.
     *
     * @return the inverse od this {@code Matrix4f}.
     * @throws UnsupportedOperationException if the inverse does not exist;
     *                                       the determinant is equal to 0.
     */
    public Matrix4f inverse() {
        if (determinant() == 0) {
            throw new UnsupportedOperationException("The determinant of this matrix is equal to 0;" +
                    "the inverse does not exist.");
        }
        return adjugate().scale(1 / determinant());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Matrix4f matrix4f)) return false;
        return getV1().equals(matrix4f.getV1())
                && getV2().equals(matrix4f.getV2())
                && getV3().equals(matrix4f.getV3())
                && getV4().equals(matrix4f.getV4());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getV1(), getV2(), getV3(), getV4());
    }

    @Override
    public String toString() {
        return getV1().getX() + "  " + getV2().getX() + "  " + getV3().getX() + "  " + getV4().getX() + "\n" +
                getV1().getY() + "  " + getV2().getY() + "  " + getV3().getY() + "  " + getV4().getY() + "\n" +
                getV1().getZ() + "  " + getV2().getZ() + "  " + getV3().getZ() + "  " + getV4().getZ() + "\n" +
                getV1().getW() + "  " + getV2().getW() + "  " + getV3().getW() + "  " + getV4().getW();
    }
}