package io.github.bcherniakh.sendmoney.util;

import io.github.bcherniakh.sendmoney.exception.SendMoneyException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * <p>This utility class helps to validate data in the application workflow</p>
 * <p>Validation based on the following principle:
 * <ul>
 * <li>An invalid argument causes a subtype of the {@link SendMoneyException}</li>
 * </ul>
 *
 * @author Bohdan Cherniakh
 */
public final class Validation {

    private Validation() {
    }

    /**
     * Validates that the specified argument is not {@code null}.
     * In other case builds and throws {@link SendMoneyException} subtype.
     *
     * @param value             the value for validation
     * @param exceptionSupplier the {@link Supplier} for the exception that will be
     *                          thrown if the validation fails
     * @param <E>               the exception type
     */
    public static <E extends SendMoneyException> void notNull(final Object value, final Supplier<E> exceptionSupplier) {
        if (Objects.isNull(value)) {
            throw exceptionSupplier.get();
        }
    }

    /**
     * Validates that the argument condition is {@code true}. In other case
     * builds and throws {@link SendMoneyException} subtype.
     *
     * @param expression        the boolean expression to check
     * @param exceptionSupplier the {@link Supplier} for the exception that will be
     *                          thrown if the validation fails
     * @param <E>               the exception type
     */
    public static <E extends SendMoneyException> void isTrue(final boolean expression, final Supplier<E> exceptionSupplier) {
        if (!expression) {
            throw exceptionSupplier.get();
        }
    }

    /**
     * Validates that the argument condition is {@code false}. In other case
     * builds and throws {@link SendMoneyException} subtype.
     *
     * @param expression        the boolean expression to check
     * @param exceptionSupplier the {@link Supplier} for the exception that will be
     *                          thrown if the validation fails
     * @param <E>               the exception type
     */
    public static <E extends SendMoneyException> void isFalse(final boolean expression, final Supplier<E> exceptionSupplier) {
        isTrue(!expression, exceptionSupplier);
    }

    /**
     * Validates that the specified argument collection is neither {@code null}
     * nor empty. In other case builds and throws {@link SendMoneyException} subtype.
     *
     * @param collection        the collection for validation
     * @param exceptionSupplier the {@link Supplier} for the exception that will be
     *                          thrown if the validation fails
     * @param <T>               the collection type
     * @param <E>               the exception type
     */
    public static <T extends Collection<?>, E extends SendMoneyException> void notEmpty(final T collection, final Supplier<E> exceptionSupplier) {
        if (CollectionUtils.isEmpty(collection)) {
            throw exceptionSupplier.get();
        }
    }

    /**
     * Validates that the specified {@code string} is not empty. In other case
     * builds and throws {@link SendMoneyException} subtype.
     *
     * @param string            the string value to check
     * @param exceptionSupplier the {@link Supplier} for the exception that will be
     *                          thrown if the validation fails
     * @param <E>               the exception type
     */
    public static <E extends SendMoneyException> void notEmpty(final String string, final Supplier<E> exceptionSupplier) {
        if (StringUtils.isEmpty(string)) {
            throw exceptionSupplier.get();
        }
    }

    /**
     * Validates that the specified {@code string} is empty. In other case
     * builds and throws {@link SendMoneyException} subtype.
     *
     * @param string            the string value to check
     * @param exceptionSupplier the {@link Supplier} for the exception that will be
     *                          thrown if the validation fails
     * @param <E>               the exception type
     */
    public static <E extends SendMoneyException> void isEmpty(final String string, final Supplier<E> exceptionSupplier) {
        if (StringUtils.isNotEmpty(string)) {
            throw exceptionSupplier.get();
        }
    }
}
