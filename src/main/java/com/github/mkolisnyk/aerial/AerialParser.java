/**
 * .
 */
package com.github.mkolisnyk.aerial;

/**
 * @author Myk Kolisnyk
 *
 */
public interface AerialParser<T> {
    T parse(String input) throws Exception;
    void validate(String input);
}
